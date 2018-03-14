<%@tag pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="title"%>
<%@attribute name="head" fragment="true"%>
<%@attribute name="content" fragment="true"%>
<%@attribute name="script" fragment="true"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

        <link rel="stylesheet" href="<c:url value="/css/main.css"/>" />
        <link rel="stylesheet" href="<c:url value="/css/form.css"/>" />
        
        <title>Haushaltsbuch</title>

        <jsp:invoke fragment="head"/>
    </head>
    <body>
        <%-- Kopfbereich --%>
        <header>

            <%-- Menü --%>
            <nav class="navbar sticky-top navbar-expand-lg navbar-dark bg-dark">
                <a class="navbar-brand" href="#">Haushaltsbuch</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav">
                        
                        <li class="nav-item">
                            <a class="nav-link" href="#">Transaktionen anzeigen</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Transaktion anlegen</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/haushaltsbuch/app/statistik/">Transaktionen auswerten</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Kategorien bearbeiten</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Ausloggen</a>
                        </li>
                        
                        
                    </ul>
                </div>
            </nav>
        </header>

        <%-- Hauptinhalt der Seite --%>
        <main>    
            <jsp:invoke fragment="content"/>
        </main>

        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
        
        <jsp:invoke fragment="script"/>
        
        
        
        
        
    </body>
</html>