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
    <link rel="stylesheet" href="style/pages-style/my-courses-page.css">
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
            <div class="my-courses-container">
            <form class="search-form" action="post">
            <input type="hidden" name="command" value = "filter"/>
            <input type="hidden" name="type" value = "${type}"/>
                    <input type="search" name="name">
                    <button type="submit">Поиск</button>
             </form>
            <c:if test = "${type=='subs'}">
                <div class="courses-header">
                    <h4>МОИ ПОДПИСКИ</h4>
                </div>          
            </c:if> 
			<c:if test = "${type=='teach'}">
                <div class="courses-header">
                    <h4>МОИ КУРСЫ</h4>
                </div>
                 <div align="center"><button onclick="controller?command=get_cources_command&type=subs&method=POST">Мои подписки</button></div>          
            </c:if>
            
            <c:if test="${role=='admin'}">
            	<c:if test="${type=='admin_banned'}">
            	<div class="courses-header">
                    <h4>СПИСОК ЗАБАНЕНЫХ</h4>
                </div>
            	 <div align="center"><button onclick="controller?command=get_cources_command&type=admin_unbanned&method=POST">Список разбаненых</button></div>
            	</c:if>
            	<c:if test="${type=='admin_unbanned'}">
            	<div class="courses-header">
                    <h4>СПИСОК РАзБАНЕНЫХ</h4>
                </div>
            	 <div align="center"><button onclick="controller?command=get_cources_command&type=admin_banned&method=POST">Список забаненых</button></div>
            	</c:if>
            	<c:if test="${type=='admin_all'}">
            	<div class="courses-header">
                    <h4>ВСЕ КУРСЫ</h4>
                </div>
            	 <div align="center"><button onclick="controller?command=get_cources_command&type=admin_unbanned&method=POST">Список разбаненых</button></div>
            	 <div align="center"><button onclick="controller?command=get_cources_command&type=admin_banned&method=POST">Список забаненых</button></div>
            	</c:if>
            </c:if>
            <c:if test="${role!='admin'}">
                <div align="center"><button onclick="controller?command=get_cources_command&type=recoms&method=POST">Рекомендованные курсы</button></div>
 			</c:if>

<!--                                Шаблон созданного курса-->
				<c:forEach var="course" items="${courses}">
                <div class="course">
                    <div class="col-2 text-center">
                        <img class="course-img" src="img/logo.png" alt="">
                    </div>

                    <div class="col-10 course-details">
                        <h5>${course.name}</h5>

                        <p class="course-description">
                            ${course.desctiprion}
                        </p>

                        <div class="course-stats">
                            <!--  <div class="row">
                                <p>0 занятий</p>
                                <p>0 участников</p>
                            </div>-->

                            <div class="row">
                            	<c:if test="${type=='teach'}">
                                <button class="edit-button" onclick="controller?command=forward_to_update_course&course_id=${course.id}&method=POST">РЕДАКТИРОВАТЬ</button>
                                <button class="edit-button" onclick="controller?command=delete_course&course_id=${course_id}&method=POST">УДАЛИТЬ КУРС</button>
                                </c:if>
                                <c:if test="${type=='subs'}">
                                <button class="edit-button" onclick="controller?command=display_course&course_id=${course.id}&method=POST">ПРОХОЖДЕНИЕ</button>
                                <button class="edit-button"onclick="controller?command=unsubscribe&course_id=${course.id}&method=POST">ОТПИСАТЬСЯ</button>
                                </c:if>
                                <c:if test="${type=='recoms'}">
                                <button class="edit-button" onclick="controller?command=get_course_info_command&course_id=${course.id}">ПОДРОБНЕЕ</button>
                                <button class="edit-button" onclick="controller?command=subscribe&course_id=${course.id}">ПОДПИСАТЬСЯ</button>
                                </c:if>
                                <c:if test="${role=='admin'}">
                                <button class="edit-button" onclick="controller?command=get_course_info_command&course_id=${course.id}">Подробнее</button>
                               <c:if test="${course.verification==0}">
                                <button class="edit-button" onclick="controller?command=unban_course&course_id=${course.id}">Разбанить</button>
                                </c:if>
                                </c:if>
                            </div>
                            <c:if test="${course.verification==1}">
                                <button class="edit-button" onclick="controller?command=unban_course&course_id=${course.id}">Разбанить</button>
                                </c:if>
                        </div>
                    </div>
                </div>
                </c:forEach>
            
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
