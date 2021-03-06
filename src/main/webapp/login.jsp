<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
<%--    <link rel="stylesheet" href="css/login.css" type="text/css">--%>
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <title>Авторизация</title>
</head>
<body>
<div class="container pt-3">

    <div class="row">
        <div class="card">
            <div class="card-header">
                Авторизация
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/auth.do" class="form-auth" method="post">
                    <div class="form-email">
                        <label>Почта</label>
                        <input type="text" class="form-control" name="email">
                    </div>
                    <div class="form-password">
                        <label>Пароль</label>
                        <input type="text" class="form-control" name="password">
                    </div>
                    <br>
                    <button type="submit" class="btn-btn-primary" id="submitButton">Войти</button>
                    <a href="reg.do" class="reg-link" id="reg_link">
                        Зарегистрироваться!
                    </a>
                    <c:if test="${not empty error}">
                        <div style="color:red; font-weight: bold; margin: 30px 0;">
                                ${error}
                        </div>
                    </c:if>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
