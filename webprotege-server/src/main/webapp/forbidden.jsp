<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String acceptLanguage = request.getHeader("Accept-Language");
    boolean zh = acceptLanguage != null && acceptLanguage.toLowerCase().startsWith("zh");
%>
<html>
<head>
    <title><%= zh ? "禁止访问" : "Forbidden" %></title>
    <link rel="stylesheet" href="css/WebProtege.css">
    <style type="text/css">
        .main {
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            box-sizing: border-box;
            display: flex;
            flex-direction: column;
            align-content: center;
            align-items: center;
            justify-content: center;
            padding: 10px;
        }
    </style>
</head>
<body>

<div class="main">
    <img src="img/protege-blender-monochrome.png" alt="Forbidden" style="width: 100px; position: relative; left: -10px;">
    <h1><%= zh ? "禁止访问" : "Forbidden" %></h1>
</div>
</body>
</html>
