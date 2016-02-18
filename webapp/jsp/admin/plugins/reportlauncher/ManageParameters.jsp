<jsp:useBean id="managereportlauncherParameter" scope="session" class="fr.paris.lutece.plugins.reportlauncher.web.ParameterJspBean" />
<% String strContent = managereportlauncherParameter.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
