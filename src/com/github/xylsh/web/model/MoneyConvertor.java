package com.github.xylsh.web.model;

/**
 * MoneyConvertor 将小写数字金额转换为中文大写金额。<br />
 * 数字金额整数部分最多12位，小数部分最多3位。
 */
public class MoneyConvertor {

    private final String[] NUMBER_STR = { "零", "壹", "贰", "叁", "肆", "伍", "陆",
            "柒", "捌", "玖" };
    private final String[] I_UNIT_STR = { "元", "拾", "佰", "仟", "万", "拾", "佰",
            "仟", "亿", "拾", "佰", "仟" };
    private final String[] D_UNIT_STR = { "角", "分", "厘" };

    private String numberString = null;          // 小写金额
    private StringBuffer resultString = null;    // 大写金额

    /**
     * 使用一个小写数字金额的字符串来构造MoneyConvertor对象
     * 
     * @param numberStr
     *            将要转换的小写数字金额
     * @throws IllegalArgumentException
     *            如果小写金额不合法(即isLegal()返回false),无法转换,则抛出IllegalArgumentException
     */
    public MoneyConvertor(String numberStr) throws IllegalArgumentException {
        if (!isLegal(numberStr)) {
            throw new IllegalArgumentException("Illegal money number.");
        } else {
            this.numberString = new StringBuffer(numberStr).reverse()
                    .toString().trim();
            resultString = new StringBuffer();
        }
    }

    /**
     * 将小写数字金额转换为中文大写金额
     * 
     * @return 转换过后的中文大写金额
     */
    public String convert() {
        preProcessing();                                      // 预处理小写金额numberString
        String[] cutedNumber = numberString.split("\\.");     // 整数和小数部分 分开 分别进行转换
        if (cutedNumber.length == 1) {                        // 如果只有整数部分
            convertInteger(numberString);
        } else {                                              // 既有整数部分，又有小数部分
            // 因为convertDecimal()是把转换过后的字符串append()到resultString的,
            // 而resultString先要保存整数部分的大写，所以下面2行不能换顺序
            convertInteger(cutedNumber[1]);
            convertDecimal(cutedNumber[0]);
        }
        removeZero(resultString);            // 去掉多余的"零"
        checkHead(resultString);             // 如果转换过后的金额前几个字符没有意义，则去掉
        resultString.append("整");
        return resultString.toString();
    }

    /**
     * 判断用户输入的小写金额是否合法
     * 
     * @param numberStr
     *            小写金额
     * @return 合法返回true,非法返回false
     */
    public boolean isLegal(String numberStr) {
        /*
         * 有两种方法验证 数字金额字符串numberString 是否合法:
         *     1.一项一项条件的判断(更容易理解) 
         *     2.使用正则表达式匹配(更简洁)
         * 
         * 这里采用第二种方法。
         */
        // 下面是第二种方法
        if (numberStr == null || numberStr.isEmpty()) { // 如果numberStr为NULL，则isEmpty()不会执行，所以不会产生NullPointerException
            return false;
        }
        String regex = "\\d{0,12}(\\.|\\.\\d{0,3})?";
        if (!numberStr.matches(regex)) {
            return false;
        }

        return true;

        /*下面是第一种方法
        //1.不能为空
        if( numberStr==null || numberStr.isEmpty()){
            return false;
        }
        //2.不能有非法字符
        short dotCount = 0;   //小数点数量
        for(int i=0;i<numberStr.length();i++){
            char c = numberStr.charAt(i);
            switch (c){  
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': 
                case '9': {  
                              break;
                }
                case '.': {
                              dotCount++;
                              break;
                }
                default: {    return false;
                }  
            }
        }
        //3.小数点不能超过1个
        if(dotCount>1){
            return false;
        }
        //4.整数部分最多12位，小数部分最多3位
        String[] cutedNumber = numberStr.split("\\.");
        if(cutedNumber.length == 1 && cutedNumber[0].length() >12){
            return false;
        }else if(cutedNumber.length == 2 && (cutedNumber[0].length()>12 || cutedNumber[1].length()>3) ){  
            return false;
        }
        
        return true;*/
    }

    /**
     * 有些小写金额有些特殊，譬如".5"，处理它们要先把它们变成"0.5",否则后面转换会出错
     */
    private void preProcessing() {
        if (numberString.charAt(numberString.length() - 1) == '.') {
            numberString += '0';
        }
    }

    /**
     * 替换StringBuffer中的字符串former
     * 
     * @param source
     *            将被替换内容的字符串
     * @param former
     *            将替换的子字符串
     * @param later
     *            将替换原有内容的字符串
     */
    private void replaceStr(StringBuffer source, String former, String later) {
        int index = source.indexOf(former);
        while (index != -1) {
            source.replace(index, index + former.length(), later);
            index = source.indexOf(former);
        }
    }

    /**
     * 去掉多余的"零X"
     * 
     * @param resultString
     *            要操作的字符串
     */
    private void removeZero(StringBuffer resultString) {
        if (resultString.toString().equals("零元零角")
                || resultString.toString().equals("零元")) {
            return;
        }
        // replaceStr(resultString,"零元","元");
        replaceStr(resultString, "零拾", "零");
        replaceStr(resultString, "零佰", "零");
        replaceStr(resultString, "零仟", "零");
        replaceStr(resultString, "零万", "万");
        replaceStr(resultString, "零亿", "亿");
        replaceStr(resultString, "零角", "零");
        replaceStr(resultString, "零分", "零");
        replaceStr(resultString, "零厘", "零");
        replaceStr(resultString, "零零", "零");
        replaceStr(resultString, "亿万", "亿");
        replaceStr(resultString, "零元", "元");
        if (resultString.charAt(resultString.length() - 1) == '零') {
            resultString.deleteCharAt(resultString.length() - 1);
        }
    }

    /**
     * 如果转换过后的金额前几个字符没有意义，则去掉
     * 
     * @param resultString
     *            要操作的字符串
     */
    private void checkHead(StringBuffer resultString) {
        if (resultString.toString().equals("零元零角")
                || resultString.toString().equals("零元")) {
            return;
        }
        char temp = resultString.charAt(0);
        while (temp == '零' || temp == '元' || temp == '拾' || temp == '佰'
                || temp == '仟' || temp == '万' || temp == '亿' || temp == '厘'
                || temp == '分' || temp == '角') {
            resultString.deleteCharAt(0);
            temp = resultString.charAt(0);
        }
    }

    /**
     * 转换整数部分
     * 
     * @param integerString
     *            要操作的字符串
     */
    private void convertInteger(String integerString) {
        for (int i = 0; i < integerString.length(); i++) {
            resultString.append(I_UNIT_STR[i]);
            resultString.append(NUMBER_STR[integerString.charAt(i) - 48]);
        }
        resultString.reverse();
    }

    /**
     * 转换小数部分
     * 
     * @param decimalString
     *            要操作的字符串
     */
    private void convertDecimal(String decimalString) {
        StringBuffer temp = new StringBuffer();
        int j = decimalString.length() - 1;
        for (int i = 0; i < decimalString.length(); i++) {
            temp.append(D_UNIT_STR[j]);
            temp.append(NUMBER_STR[decimalString.charAt(i) - 48]);
            j--;
        }
        temp.reverse();
        resultString.append(temp);
    }

}
