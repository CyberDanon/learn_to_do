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
    <link rel="stylesheet" href="style/pages-style/edit-course-page.css">
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
            <div class="edit-course-container">
                <div class="editing-course">
                    <h4>РЕДАКТИРОВАНИЕ КУРСА</h4>

                    <form class="editing-form" action="controller" method="post">
                    	<input type="hidden" name="command" value = "update_course"/>
                    	<input type="hidden" name="course_id" value="${course.id}"/>
                        <label for="name">Название курса (3-30 символов):</label>
                        <input type="text" name="name" id="name" pattern="^\.{3,30}$" required>

                        <label for="descr">Описание курса: (не более двухсот символов)</label>
                        <textarea name="description" id="descr" cols="20" rows="5" maxlength="200"></textarea>

                        <label for="img">Изображение для превью курса:</label>
                        <input type="file" name="image" id="img" accept="image/*">

                        <div class="submit-button">
                            <button type="submit">ПРИМЕНИТЬ</button>
                        </div>
                    </form>

                    <div class="lessons-container">
                        <div class="row">
                            <h5>ЗАНЯТИЯ</h5>
                            <i class="fas fa-info-circle info-icon"
                                title="Курс состоит из занятий. Каждое занятие содержит текст. Также можно добавить тесты для занятия">
                            </i>
                        </div>

<!--                        Шаблон Занятия-->
					<c:forEach var="theor" items="${course.th}">
                        <div class="lesson">
                            <div class="lesson-header">
                                <div class="name">"${th.name}"</div>
                                <div class="editing-buttons">
                                    <button onclick = "controller?command=forward_to_update_theory&theory_id=${th.id}&method=POST">Редактировать занятие</button>
                                    <button onclick = "controller?command=create_test&source_type=theory&source_id=${th.id}&method=POST"">Добавить тест</button>
                                </div>
                            </div>
						
<!--                            Шаблон теста для занятия-->
                            <div class="local-test-header">
                            	<c:forEach var="local_test" items="${th.tests}">
                                	<div class="name">"${local_test.name}"</div>
    	                            <div class="editing-buttons">
        	                            <button onclick = "/controller?command=forward_to_update_test&course_id=${course.id}$test_id=${local_test.id}&method=POST">Редактировать тест</button>
            	                    </div>
            	                </c:forEach>
                            </div>
                        </div>
                        </c:forEach>
                        <button class="add-item-button">Добавить занятие</button>
                    </div>

                    <div class="global-tests-container">
                        <div class="row">
                            <h5>ГЛОБАЛЬНЫЕ ТЕСТЫ</h5>
                            <i class="fas fa-info-circle info-icon"
                                title="Это глобальные тесты курса, они не привязаны к занятиям">
                            </i>
                        </div>

<!--                        Шаблон глобального теста-->
                        <div class="global-test">
                        <c:forEach var="global_tes" items="${course.test}">
                            <div class="global-test-header">
                                <div class="name">"${global_test.name}"</div>
                                <div class="editing-buttons">
                                    <button onclick = "/controller?command=forward_to_update_course&course_id=${global_test.id}&course_id=${course.id}&method=POST">Редактировать тест</button>
                                </div>
                            </div>
                        </c:forEach>
                        </div>

                        <button class="add-item-button" onclick = "/controller?command=create_test&source=course&source_id=${course_id}&method=POST">Добавить тест</button>
                    </div>
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
