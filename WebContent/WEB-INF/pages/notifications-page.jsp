<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>LearnToDo</title>
    <link rel="stylesheet" href="style/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="style/styles.css">
    <link rel="stylesheet" href="style/pages-style/notifications-page.css">
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
            <button onclick = "/controoler?command=main&method=POST">ГЛАВНАЯ</button>
            <button onclick = "/controller?command=get_cources_command&method=POST">КУРСЫ</button>
            <button onclick = "/cotroller?command=users_top&method=POST">ТОП УЧАЩИХСЯ</button>
            <button onclick= "/controller?command=faq&method=POST">FAQ</button>
        </div>
    </div>

    <!--    Контент    -->
    <div class="content">
        <div class="centered">
        <c:forEach var ="notif" items = "${messages}">
            <div class="notifications-container">
            	
                <div class="notification">
				<c:if test="${notif.topic=='subs_banned'}">                
                    <h5 class="notification-header">Один из Вашихх курсов был забанен</h5>
				</c:if>
				<c:if test="${notif.topic=='subs_unbanned'}" >               
                    <h5 class="notification-header">Один из Вашихх курсов был разбанен</h5>
				</c:if>
				<c:if test="${notif.topic=='subs'}">                
                    <h5 class="notification-header">Оповещение от курса</h5>
				</c:if>
				<c:if test="${notif.topic=='teach'}">                
                    <h5 class="notification-header">Один из Вами созданных курсов был забанен</h5>
				</c:if>
				<c:if test="${notif.topic=='teach_unbanned'}">                
                    <h5 class="notification-header">Один из Вами созданных курсов был разабанен</h5>
				</c:if>
                    <div class="notification-text">
                        ${notif.text}
                    </div>

                    <div class="notification-buttons">
                    	<c:if test="${notif.topic =='subs_banned'}">
                        <button onclick="controller/command=safe_unsubscribe&course_id=${notif.course_id}&method=POST">Отписаться без потерь</button>
                        </c:if>
                        <c:if test="${notif.topic=='subs_unbanned'}">
                        <button onclick="controller/command=display_course&course_id=${notif.course_id}&method=POST">Прохождение курса</button>
                        </c:if>
                        <c:if test="${notif.topic=='teach'}">
                        <button onclick="controller/command=forward_to_update_course&course_id=${notif.course_id}&method=POST">Изменить курс</button>
                        </c:if>
                        <c:if test="${notif.topic == 'subs'}">
                        <button onclick="controller/command=display_course&course_id=${notif.course_id}&method=POST">Прохождение курса</button>
                        </c:if>
                        <button>Удалить</button>
                    </div>
                </div>
                
            </div>
            </c:forEach>
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
