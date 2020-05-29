<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"
%>
<html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>LearnToDO</title>
        <link rel="stylesheet" href="style/bootstrap/bootstrap.css">
        <link rel="stylesheet" href="style/styles.css">
        <link rel="stylesheet" href="style/pages-style/main-page.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    </head>
    <body>

     <!--    Шапка   -->
    <header>
        <div class="row profile-section">
            <div class="col-2"></div>
            <div class="col-10">
            <c:if test = "${not empty user}">
                <a href="/controller?command=main">
                    <i class="fa fa-user user-icon"></i>
                    <c:out value ="${user.login}"/>
                </a>
                <a href="/controller?command=get_messages_command">
                    <i class="fas fa-bell notification-icon"></i>
                    Уведомления <c:out value = "${notifications}"/>
                </a>
            </c:if>
            <c:if test="${empty user}">
            <a href="/controller?command=forward_login">
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
            	<c:if test="${role != admin}">
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

        <div class="row sticky-top main-menu">
        <div class="col-2"></div>

        <div class="col-10">
            <button onclick = "/controoler?command=main">ГЛАВНАЯ</button>
            <button onclick = "/controller?command=get_cources_command">КУРСЫ</button>
            <button onclick = "/cotroller?command=users_top">ТОП УЧАЩИХСЯ</button>
            <button onclick= "/controller?command=faq">FAQ</button>
        </div>
    </div>
    </div>

    <!--    Контент    -->
    <div class="content">
        <div class="welcome-container">
            <img class="main-img" src="img/main-road.jpg" alt="">

            <div class="welcome-content">
                <p>Приветствуем на образовательной платформе<br>LearnToDO!</p>
                <p>Мы обучим вас легко и быстро.</p>
            </div>
        </div>

        <div class="advantages-container">
            <h3 class="text-center">LearnToDO – это</h3>

            <div class="row">
                <div class="col-2"></div>

                <div class="col-8">
                    <div class="advantages">
                        <div class="advantage-element"><h5>НАДЕЖНОСТЬ</h5></div>
                        <div class="advantage-element"><h5>ДОСТУПНОСТЬ</h5></div>
                        <div class="advantage-element"><h5>КОМФОРТ</h5></div>
                        <div class="advantage-element"><h5>ЭФФЕКТИВНОСТЬ</h5></div>
                    </div>
                </div>

                <div class="col-2"></div>
            </div>
        </div>

        <div class="row mobile-app-container">
            <div class="col-2"></div>

            <div class="col-10 mobile-app-content">
                <img class="man-image" src="img/app-cutted.png" alt="">
                <h4>Устанавливайте наше мобильное приложение – получайте знания в любом месте</h4>
            </div>
        </div>

        <div class="start text-center">
            <h4>Более 1000 участников по всему миру. Начните путь по дороге знаний вместе с LearnToDO.</h4>
            <button action = "/controller?command=get_cources_command">УЧИТЬСЯ!</button>
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
