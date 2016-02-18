<jsp:useBean id="managereportlauncherReport" scope="session" class="fr.paris.lutece.plugins.reportlauncher.web.ReportJspBean" />
<% String strContent = managereportlauncherReport.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
