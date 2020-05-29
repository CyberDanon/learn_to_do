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
    <link rel="stylesheet" href="style//styles.css">
    <link rel="stylesheet" href="style/pages-style/course-passing-page.css">
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
                    Уведомления <c:out value = "${notifications}"?/>
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
        <div class="course-passing-container">
            <div class="course-contents-container">
                <h5 class="header">СОДЕРЖАНИЕ</h5>

                <button class="title-page-button">ГЛАВНАЯ СТРАНИЦА</button>

<!--                    ШАБЛОН ЗАНЯТИЯ В СОДЕРЖАНИИ КУРСА (ВНУТРЬ ДОБАВЛЯЮТСЯ ЛОКАЛЬНЫЕ ТЕСТЫ)-->
				<c:forEach var="theor" items="${course.th}">
                <div class="lesson-container">
                    <button class="lesson-button" onclick = "controller?command=display_theorical&theory_id=${theor.id}&method=POST">"${theor.name}"</button>

<!--                        ШАБЛОН ЛОКАЛЬНОГО ТЕСТА-->
					<c:forEach var="local_test" items="${theor.tests}">
                    <button class="test-button" onclick="controller?command=display_test&course_id=${course_id}&test_id=${local_test.id}&method=POST">"${local_test.name}"</button>
                    </c:forEach>
                </div>
				</c:forEach>
                
                <h6 class="small-header">ГЛОБАЛЬНЫЕ ТЕСТЫ</h6>
				<c:forEach var="global_test" items="${course.test}">
                <button class="test-button" onclick="controller?command=display_test&course_id=${course_id}&test_id=${global_test.id}&method=POST">"${global_test.name}"</button>
                </c:forEach>
            </div>

<!--            ШАБЛОН ОТОБРАЖЕНИЯ ТЕКСТА ЗАНЯТИЯ-->
            <div class="element-content-container">
            	<c:if test="${not empty theory}">
                <h5 class="header">Занятие 1. ${theory.name}</h5>
                
                <div class="text-justify">
                   "${theory.text}" 
                </div>
                </c:if>
                <c:if test="${not empty test_preview}">
                <h5 class="header">Тест 1. ${test_preview.name}</h5>
                
                <div class="text-justify">
                   "${test_preview.description}" 
                </div>
                <c:if test = "${empty grade}">
                <button onclick="controlller?command=forward_pass_test&test_id=${test_preview.id}&method=POST">Пройти тест</button>
                </c:if>
                <c:if test = "${not empty grade}">
                <div class="text-justify">
                   Вы уже прошли этот тест. Ваша оценка - "${grade}". 
                </div>
                </c:if>
                </c:if>
                <c:if test="${not empty test}">
                <form action="controller" method="post">
                <div class="element-content-container">-->
                    <h5 class="header">"${test.name}"</h5>-->
					<input type="hidden" name="command" value = "pass_test"/>
					<input type="hidden" name="test_id" value = "${test.id}"/>
                   <div class="text-justify">-->
                       "${test.description}"
                    </div>-->

                    <c:forEach var = "questionz" items="${test.questions}">
                    <div class="question">-->
                        <div class="question-header">${questionz.text}</div>
					   <c:forEach var="answerz" items="${questionz.answers}">
                       <div class="answer">
                            <input type="checkbox" name="answer${anwerz.id}" id="answer"
                            <c:if test = "${role == 'admin'}">
                            disabled
                            </c:if>
                            <c:if test1 = "${(answerz.right == 1)&&(role == admin)}">
                            		checked
                            </c:if>
                            >
                           <label class="no-selection" for="answer"${answerz.getid}">${answerz.text}</label>
                       </div>
                       </c:forEach>
                   </div>
                   </c:forEach>
                   <div align="center">
                   <c:if test = "${role != 'admin' }">
                   <input type="submit" value = "Отправить тест"/>
                   </c:if>
                   <c:if test = "${role == 'admin' }">
                   <button onclick = "controller?command=display_course&course_id=${course.id}&method=POST">Вернуться</button>
                   </c:if>
                   </div>
               </div>
               </form>
                </c:if>
            </div>



<!--            ШАБЛОН ТИТУЛКИ КУРСА-->
<!--            <div class="course-title-container">-->
<!--                <h3 class="header">Название курса</h3>-->

<!--                <div class="course-title-description">-->
<!--                    Lorem ipsum dolor sit amet, consectetur adipisicing elit. Debitis libero magni molestiae natus, necessitatibus quis ratione saepe sed ut voluptates. Accusantium debitis id qui quidem sit. Aperiam consectetur ipsam laudantium?-->
<!--                </div>-->

<!--                <h4 class="header">Оцените курс</h4>-->

<!--                <div id="reviewStars-input">-->
<!--                    <input id="star-4" type="radio" name="reviewStars"/>-->
<!--                    <label title="Отлично" for="star-4"></label>-->

<!--                    <input id="star-3" type="radio" name="reviewStars"/>-->
<!--                    <label title="Хорошо" for="star-3"></label>-->

<!--                    <input id="star-2" type="radio" name="reviewStars"/>-->
<!--                    <label title="Нормально" for="star-2"></label>-->

<!--                    <input id="star-1" type="radio" name="reviewStars"/>-->
<!--                    <label title="Плохо" for="star-1"></label>-->

<!--                    <input id="star-0" type="radio" name="reviewStars"/>-->
<!--                    <label title="Ужасно" for="star-0"></label>-->
<!--                </div>-->

<!--                ШАБЛОН КНОПОК НА ГЛАВНОЙ СТРАНИЦЕ КУРСА-->
<!--                <div class="manage-buttons">-->
<!--                    <button>Забанить курс</button>-->
<!--                    <button>Сделать рассылку</button>-->
<!--                    <button>Отписаться</button>-->
<!--                </div>-->
<!--            </div>-->



<!--                ШАБЛОН ОТОБРАЖЕНИЯ ТЕСТА            -->
<!--                <div class="element-content-container">-->
<!--                    <h5 class="header">Тест 1. Название теста</h5>-->

<!--                    <div class="text-justify">-->
<!--                        Описание теста Описание теста Описание теста Описание теста Описание теста Описание теста Описание теста Описание теста Описание теста Описание теста Описание теста Описание теста-->
<!--                    </div>-->

<!--                    ШАБЛОН ВОПРОСА-->
<!--                    <div class="question">-->
<!--                        <div class="question-header">Текст вопроса</div>-->

<!--                        ШАБЛОН ОТВЕТА-->
<!--                        <div class="answer">-->
<!--                            <input type="checkbox" name="answer" id="answer">-->
<!--                            <label class="no-selection" for="answer">Текст ответа</label>-->
<!--                        </div>-->
<!--                    </div>-->
<!--                </div>-->
        </div>

        <div class="up">
            <a class="stretched-link" href="#"><i class="fas fa-arrow-up"></i></a>
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
