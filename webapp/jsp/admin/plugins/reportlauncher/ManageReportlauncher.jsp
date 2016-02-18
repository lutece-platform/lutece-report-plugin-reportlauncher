<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="managereportlauncher" scope="session" class="fr.paris.lutece.plugins.reportlauncher.web.ManageReportlauncherJspBean" />

<% managereportlauncher.init( request, managereportlauncher.RIGHT_MANAGEREPORTLAUNCHER ); %>
<%= managereportlauncher.getManageReportlauncherHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
