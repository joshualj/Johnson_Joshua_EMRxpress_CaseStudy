<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../include/header.jsp"/>
<jsp:include page="../include/navbar.jsp"/>

<title>Patient Registration</title>
<link href="../../../pub/css/register.css" rel="stylesheet">

<body>
<section class="registrationSection vh-140 gradient-custom">
    <div class="container py-3 h-120">
        <div class="row justify-content-center align-items-center h-120">
            <div class="col-5">
                <div class="card shadow-2-strong card-registration" style="border-radius: 15px;">
                    <div class="card-body p-4 p-md-5">
                        <h3 class="mb-4 pb-2 pb-md-0 mb-md-5">Patient Registration</h3>
                        <form id="registerForm" action="/user/registerSubmit" class="needs-validation" novalidate>
<%--                            <input type="hidden" name="id" value="${form.id}">--%>
                            <div class="col-md-12 mb-4">
<%--                                <input type="text" id="firstName" class="form-control form-control-lg" name="firstName" value="${form.firstName}" required/>--%>
                                <input type="text" id="firstName" class="form-control form-control-lg" name="firstName" value="${form.firstName}"/>
                                <c:forEach items="${bindingResult.getFieldErrors('firstName')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                                <label class="form-label" for="firstName">First Name</label>
                            </div>

                            <div class="col-md-12 mb-4">
                                <input type="text" id="lastName" class="form-control form-control-lg" name="lastName" value="${form.lastName}"/>
                                <c:forEach items="${bindingResult.getFieldErrors('lastName')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                                <label class="form-label" for="lastName">Last Name</label>
                            </div>

                            <div class="col-md-12 mb-4">
                                <input type="text" id="preferredName" class="form-control form-control-lg" name="preferredName" value="${form.preferredName}"/>
                                <c:forEach items="${bindingResult.getFieldErrors('preferredName')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                                <label class="form-label" for="preferredName">Preferred Name</label>
                            </div>

                            <div class="col-md-12 mb-4">
                                <input type="text" id="email" class="form-control form-control-lg" name="email" value="${form.email}"/>
                                <c:forEach items="${bindingResult.getFieldErrors('email')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                                <label class="form-label" for="email">Email</label>
                            </div>

                            <div class="col-md-12 mb-4">
                                <input type="date" id="birthDate" class="form-control form-control-lg" name="birthDate" value="${form.birthDate}"/>
                                <c:forEach items="${bindingResult.getFieldErrors('birthDate')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                                <label class="form-label" for="birthDate">Birth Date</label>
<%--                                <div class="invalid-feedback">--%>
<%--                                    Please provide a valid birthdate.--%>
<%--                                </div>--%>
                            </div>


                            <div class="col-md-12 mb-4">
                                <h6 class="mb-2 pb-1">Pronouns:</h6>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="pronouns"
                                           id="sheher" value="she/her"/>
                                    <label class="form-check-label" for="sheher">she/her</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="pronouns"
                                           id="hehim" value="he/him"/>
                                    <label class="form-check-label" for="hehim">he/him</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="pronouns"
                                           id="theythem" value="they/them"/>
                                    <label class="form-check-label" for="theythem">they/them</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="pronouns"
                                           id="oth" value="other"/>
                                    <label class="form-check-label" for="oth">Other</label>
                                </div>
                                <c:forEach items="${bindingResult.getFieldErrors('pronouns')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                            </div>

<%--                            <div class="col-md-12 mb-4">--%>
<%--                                <input type="text" id="pronouns" class="form-control form-control-lg" name="pronouns" value="${form.pronouns}"/>--%>
<%--                                <c:forEach items="${bindingResult.getFieldErrors('pronouns')}" var="error">--%>
<%--                                    <div style="color: red;">--%>
<%--                                            ${error.getDefaultMessage()}--%>
<%--                                    </div>--%>
<%--                                </c:forEach>--%>
<%--                                <label class="form-label" for="pronouns">Pronouns</label>--%>
<%--                            </div>--%>

<%--                            <div class="col-md-12 mb-4">--%>
<%--                                <input type="text" id="gender" class="form-control form-control-lg" name="gender" value="${form.gender}"/>--%>
<%--                                <c:forEach items="${bindingResult.getFieldErrors('gender')}" var="error">--%>
<%--                                    <div style="color: red;">--%>
<%--                                            ${error.getDefaultMessage()}--%>
<%--                                    </div>--%>
<%--                                </c:forEach>--%>
<%--                                <label class="form-label" for="gender">Gender</label>--%>
<%--                            </div>--%>

                            <div class="col-md-12 mb-4">
                                <h6 class="mb-2 pb-1">Sex:</h6>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="sex"
                                           id="F" value="F"/>
                                    <label class="form-check-label" for="F">Female</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="sex"
                                           id="M" value="M"/>
                                    <label class="form-check-label" for="M">Male</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="sex"
                                           id="I" value="I"/>
                                    <label class="form-check-label" for="I">Intersex</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="sex"
                                           id="O" value="O"/>
                                    <label class="form-check-label" for="O">Other</label>
                                </div>
                                <c:forEach items="${bindingResult.getFieldErrors('sex')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="col-md-12 mb-4">
                                    <h6 class="mb-2 pb-1">Gender:</h6>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="gender"
                                               id="cis" value="cis"/>
                                        <label class="form-check-label" for="cis">cis</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="gender"
                                               id="trans" value="trans"/>
                                        <label class="form-check-label" for="trans">trans</label>
                                    </div>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="gender"
                                               id="othe" value="other"/>
                                        <label class="form-check-label" for="othe">other</label>
                                    </div>
                            </div>



<%--                            <div class="col-md-12 mb-4">--%>
<%--                                <input type="text" id="sex" class="form-control form-control-lg" name="sex" value="${form.sex}" required/>--%>
<%--                                <c:forEach items="${bindingResult.getFieldErrors('sex')}" var="error">--%>
<%--                                    <div style="color: red;">--%>
<%--                                            ${error.getDefaultMessage()}--%>
<%--                                    </div>--%>
<%--                                </c:forEach>--%>
<%--                                <label class="form-label" for="sex">Sex</label>--%>
<%--                                <div class="invalid-feedback">--%>
<%--                                </div>--%>
<%--                            </div>--%>

                            <div class="col-md-12 mb-4">
                                <input type="text" id="primaryLanguage" class="form-control form-control-lg" name="primaryLanguage" value="${form.primaryLanguage}"/>
                                <c:forEach items="${bindingResult.getFieldErrors('primaryLanguage')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                                <label class="form-label" for="primaryLanguage">Primary Language</label>
                            </div>

                            <div class="col-md-12 mb-4">
                                <input type="password" id="password" class="form-control form-control-lg" name="password" value="${form.password}" required/>
                                <c:forEach items="${bindingResult.getFieldErrors('password')}" var="error">
                                    <div style="color: red;">
                                            ${error.getDefaultMessage()}
                                    </div>
                                </c:forEach>
                                <label class="form-label" for="password">Password</label>
                            </div>

                            <div class="col-md-12 mb-4">
                                <input type="password" id="confirmPassword" class="form-control form-control-lg"
                                       onkeyup='check();' name="confirmPassword" value="${form.confirmPassword}"required/>
                                <c:forEach items="${bindingResult.getGlobalErrors()}" var="error">
                                <div style="color: red;">
                                    ${error.getDefaultMessage()}
                                </div>
                                </c:forEach>
                                <label class="form-label" for="confirmPassword">Confirm Password</label>
                                <div class="col-md-6 mt-4 pt-2" id="message"></div>
                            </div>
<%--                            </div>--%>
<%--                            <div class="col-md-12 mb-4">--%>
<%--                                <h6 class="mb-2 pb-1">User Type:</h6>--%>
<%--                                <div class="form-check form-check-inline">--%>
<%--                                    <input class="form-check-input" type="radio" name="inlineRadioOptions"--%>
<%--                                           id="admin" value="option1"/>--%>
<%--                                    <label class="form-check-label" for="admin">Admin</label>--%>
<%--                                </div>--%>

<%--                                <div class="form-check form-check-inline">--%>
<%--                                    <input--%>
<%--                                            class="form-check-input" type="radio" name="inlineRadioOptions"--%>
<%--                                            id="user" value="option2" checked />--%>
<%--                                    <label class="form-check-label" for="user">User</label>--%>
<%--                                </div>--%>
<%--                            </div>--%>

<%--                            <div class="col-6">--%>
<%--                                <select class="select form-control-md">--%>
<%--                                    <option value="1" disabled>Choose option</option>--%>
<%--                                    <option value="2">Patient</option>--%>
<%--                                    <option value="3">Clinician</option>--%>
<%--                                    <option value="4">Administrator</option>--%>
<%--                                </select>--%>
<%--                                <label class="form-label select-label">Choose User Type</label>--%>
<%--                            </div>--%>

                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="confirmRemove" id="confirmRemove" value="remove"/>
                                    <label class="form-check-label" for="confirmRemove">I confirm the above information is accurate</label>
                                </div>
<%--                            </div>--%>
                            <c:forEach items="${bindingResult.getFieldErrors('confirmRemove')}" var="error">
                                <div style="color: orangered;">
                                    Confirmation is required.
                                </div>
                            </c:forEach>
                            <div class="row">
                                <div class="col-md-3 mt-4 pt-2">
                                    <input class="btn btn-outline-primary btn-md" type="submit" value="Submit" />
                                </div>
                                <div class="col-md-3 mt-4 pt-2">
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>

<%-- TODO: UNDO COMMENTING OUT THE FOLLOWING CODE--%>

<script src= "../../../pub/js/register.js"></script>

<jsp:include page="../include/footer.jsp"/>
