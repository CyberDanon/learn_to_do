<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"
%>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>LearnToDo</title>
    <link rel="stylesheet" href="style/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="style/styles.css">
    <link rel="stylesheet" href="style/pages-style/sign-up-page.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css">
</head>
    <body>
    <!--    Шапка   -->
    <header>
        <div class="row profile-section">
            <div class="col-2"></div>
            <div class="col-10">
                <c:if test = "${not empty user}">
                <a href="/controller?command=main&method=POST">
                    <i class="fa fa-user user-icon"></i>
                    <c:out value ="${user.login}"/>
                </a>
                <a href="/controller?command=get_messages_command&method=POST">
                    <i class="fas fa-bell notification-icon"></i>
                    Уведомления <c:out value = "${notifications}"/>
                </a>
            </c:if>
            <c:if test="${empty user}">
            <a href="/controller?command=forward_login&method=POST">
                    <i class="fa fa-user user-icon"></i>
                    Логин
                </a>
            </c:if>
            </div>
        </div>

        <div class="row header-logo">
            <div class="col-2"></div>

            <div class="col-6">
                <a href=""><h1>Learn<span>To</span>DO</h1></a>
            </div>

            <c:if test="${not empty user}">
            	<c:if test="${role != 'admin'}">
            	<div class="col-2 wallet">
                	Баланс: "${user.balance}" грн
            	</div>
            	</c:if>
            </c:if>


            <div class="col-2"></div>
        </div>
    </header>

    <!--    Меню    -->
    <div class="row sticky-top main-menu">
        <div class="col-2"></div>

        <div class="col-10">
            <div class="col-10">
            <button onclick = "/controoler?command=main&method=POST">ГЛАВНАЯ</button>
            <button onclick = "/controller?command=get_cources_command&method=POST">КУРСЫ</button>
            <button onclick = "/cotroller?command=users_top&method=POST">ТОП УЧАЩИХСЯ</button>
            <button onclick= "/controller?command=faq&method=POST">FAQ</button>
        </div>
        </div>
    </div>

    <!--    Контент    -->
    <div class="content">
        <div class="centered">
            <div class="sign-up-form-container">
                <h4>РЕГИСТРАЦИЯ</h4>

                <form class="sign-up-form" action="controller" method="post">
                    <label for="name">
                        Имя пользователя (знак подчеркивания, кириллические и латинские буквы, не менее трех символов):
                    </label>
                    <input type="hidden" name="command" value="register">
                    <input type="text" name="login" id="name" pattern="^\.{3,}$" required>

    <!--                pattern="^\w+\@\w+\.+\w+$"  на всякий для эмейла-->
                    <label for="email">Электронная почта:</label>
                    <input type="email" name="email" id="email" required>

                    <label for="password">Пароль (не менее шести символов):</label>
                    <input type="password" name="pass" id="password" pattern="[0-9a-zA-Z!@#$%^&*]{6,}" required>

                    <div class="submit-button">
                        <button type="submit">ЗАРЕГИСТРИРОВАТЬСЯ</button>
                    </div>
                </form>

                <div class="row">
                    <div class="col-3">Есть аккаунт?</div>
                    <div class="col-3"><a href="controller?command=forward_login&method=POST">Войти</a></div>
                    <div class="col-6"></div>
                </div>
            </div>
        </div>
    </div>

    <!--    Футер   -->
    <footer>
        <div class="row top">
            <div class="col-1"></div>

            <div class="col-3">
                <div class="text-center">
                    <p>Обратная связь:</p>
                    <p>+380-68-111-11-11</p>
                    <p>learntodo@gmail.com</p>
                </div>
            </div>

            <div class="col-4"></div>

            <div class="col-3">
                <div class="text-center">
                    <p>LearnToDO</p>
                </div>
            </div>

            <div class="col-1"></div>
        </div>

        <div class="bottom text-center">
            ©2020 LearnToDO, все права защищены
        </div>
    </footer>
    </body>
</html>
