<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>

<template:base>
    <jsp:attribute name="content">  

        <div class="container">

            <div class="row">
                Transaktionen aus XML laden
            </div>
            <br/>
            <div class="row">

                <form method="POST"  enctype="multipart/form-data" >
                    XML-Datei:
                    <input type="file" name="file" id="file" /> <br/>

                    </br>
                    <button type="submit" name="action" value="create" class="icon-pencil">
                        XML-Datei einlesen
                    </button>
                    <!---
                    <input type="submit" value="Upload" name="upload" id="upload" /> -->
                </form>

            </div>
            <br/>
            <div class="row">
                Hochladen-Status:
            </div>
            <div class="row">
                <input value="${name}" size="30" placeholder="Bereit" readonly>
            </div>
        </div>   


    </jsp:attribute> 
</template:base>