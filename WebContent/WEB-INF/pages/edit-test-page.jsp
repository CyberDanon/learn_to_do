<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LearnToDo</title>
    <link rel="stylesheet" href="style/bootstrap/bootstrap.css">
    <link rel="stylesheet" href="style/styles.css">
    <link rel="stylesheet" href="style/pages-style/edit-test-page.css">
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
            <div class="edit-test-container">
            <form class="test-edit-form" action="controller" method="POST">
                <div class="editing-test">
                    <h4 class="header">РЕДАКТИРОВАНИЕ ТЕСТА</h4>

                    
                        <div class="test-metadata">
                        	<input type="hidden" name="command" value="update_test"/>
                        	<input type="hidden" name="course_id" value="${course_id}"/>
                        	<input type="hidden" name="test_id" value="${test_id}"/>
                            <label for="name">Название теста:</label>
                            <input type="text" name="name" id="name" required>

                            <label for="test-descr">Описание теста (необязательно):</label>
                            <textarea name="text" id="description" rows="10" required wrap="hard"></textarea>
                        </div>

                        <hr class="line">
						<c:set var = "i" scope = "request" value = "${1}"/>
                        <!--                        Шаблон вопроса-->
                        <c:forEach var="questionz" items = "${test.questions}">
                        <div class="question-block">
                        
                            <div class="question-header">
                                <label for="question${questionz.id}">Текст вопроса теста:</label>
                                <div class="score-count">
                                    <label for="score">Количество баллов:</label>
                                    <input type="number" name="question${questionz.id}_rac" id="score" value="0" min="0" step="1">
                                </div>
                            </div>
                            <input type="text" name="question${questionz.id}_text" id="question${questionz.id}" required>
                            <div class="delete-button">
                                <button type="button" onclick="controller?command=delete_question&question_id=${questionz.id}&method=POST">Удалить вопрос</button>
                            </div>
<!--                        Шаблон ответа-->
							<c:forEach var="answerz" items="${questionz.answers}">
                            <div class="answer-header">
                                <label for="answer${answerz.id}">Текст варианта ответа:</label>

                                <div class="row">
                                    <input type="checkbox" name="answer${answerz.id}_right" id="isRight">
                                    <label class="no-selection" for="isRight">Правильный ответ</label>
                                </div>
                            </div>

                            <input type="text" name="answer" id="answer${answerz.id}" required>

                            <div class="delete-button">
                                <button type="button" onclick="controller?command=delete_answer&answer_id=${answerz.id}&method=POST">Удалить ответ</button>
                            </div>
<!--                            Конец ответа-->
							
							</c:forEach>
                            <div class="add-answer-button">
                                <button type="button" onclick="/controller?command=create_answer&question_id=${questionz.id}&method=POST">Добавить вариант ответа</button>
                            </div>
							
                            <hr class="line">
                        </div>
						
						</c:forEach>
						</div>
                        <div class="add-question-button">
                            <button type="submit" onclick="controller?command=create_question&test_id="${test.id}&method=POST">Добавить вопрос</button>
                        </div>
						
                        <div class="submit-button">
                            <button type="submit">СОХРАНИТЬ</button>
                        </div>
                        
                    </form>
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
