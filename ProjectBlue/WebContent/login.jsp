<%@page import="Session.SessionManager"%>
<%@page import="HttpRequest.HttpParameterData"%>
<%@ page
     language="java"
     contentType="text/html; charset=UTF-8"
     pageEncoding="UTF-8"
%>
<!DOCTYPE html>
<html>


<!--                    java code               -->


<!--					head					-->
<head>
<meta charset="EUC-KR">
<meta
     http-equiv="X-UA-Compatible"
     content="IE=edge"
>
<meta
     name="viewport"
     content="width=device-width, initial-scale=1, shrink-to-fit=no"
>
<meta
     name="description"
     content=""
>
<meta
     name="author"
     content=""
>
<title>Project Blue Billiard</title>

<!-- Custom fonts for this template-->
<link
     href="vendor/fontawesome-free/css/all.min.css"
     rel="stylesheet"
     type="text/css"
>
<link
     href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
     rel="stylesheet"
>

<!-- Custom styles for this template-->
<link
     href="css/sb-admin-2.min.css"
     rel="stylesheet"
>
</head>


<!--					script					-->


<!--	                 body	                 -->
<body style="background:#6495ed">
     <div class="container">
          <!-- Outer Row -->
          <div class="row justify-content-center">
               <div class="col-xl-10 col-lg-12 col-md-9">
                    <div class="card o-hidden border-0 shadow-lg my-5">
                         <div class="card-body p-0">
                              <!-- Nested Row within Card Body -->
                              <div class="row">
                                   <div class="col-lg-6 d-none d-lg-block"></div>
                                   <div class="col-lg-6">
                                        <div class="p-5">
                                             <div class="text-center">
                                                  <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
                                             </div>
                                             <form class="user">
                                                  <br> <br> <br> <br> <br>
                                                  <hr>
                                                  <a
                                                       href=<%=HttpParameterData.mappingQueryStringAuthoCodeReceive()%>
                                                       class="btn btn-user btn-block bg-blue"
                                                       style="background:#6495ed; color:#FFFFFF;"
                                                  > <i class="fas fa-comment"></i> <%
 	if (session.getAttribute("session_manager") != null) {
 	out.write("Restart");
 } else {
 	out.write("Login with Kakao");
 }
 %>
                                                  </a>
                                             </form>
                                             <hr>
                                        </div>
                                   </div>
                              </div>
                         </div>
                    </div>
               </div>
          </div>
     </div>

     <!-- Bootstrap core JavaScript-->
     <script src="vendor/jquery/jquery.min.js"></script>
     <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

     <!-- Core plugin JavaScript-->
     <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

     <!-- Custom scripts for all pages-->
     <script src="js/sb-admin-2.min.js"></script>

     <script>
			history.pushState(null, null, location.href);
			window.onpopstate = function() {
			    history.go(1);
			};
		    </script>

</body>
</html>