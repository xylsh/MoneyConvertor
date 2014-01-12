<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>金额大小写转换</title>
<link href="./css/index.css" rel="stylesheet" type="text/css" media="all">
<link href="./css/footer.css" rel="stylesheet" type="text/css" media="all">
</head>
<body>
    <div class="header">
        <h1 align="center" class="title">
            <a href="./">Money Convertor</a>
        </h1>
    </div>
    
    <div class="form">
        <form method="get" action="convertmoney.do">
            <b>小写金额 : </b> <input class="moneytext" type="text" name="money"
                value="${ requestScope.money }" title="金额整数部分最多12位，小数部分最多3位">
            <input type="submit" value="Convert Now!">
        </form>

        <b>大写金额 : </b> <input class="resulttext" type="text"
            value="${ requestScope.result }" readonly>
    </div>
    
<!-- footer -->
<jsp:include page="./footer.jsp"></jsp:include>
<!-- footer -->

</body>
</html>
