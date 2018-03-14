<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>

<template:base>

    <jsp:attribute name="content">

        <div class="container">

            <div class="row">

                <h3>Statistiken</h3>

            </div>

            <div class="row">


                <div class="col-lg-6">

                    <canvas id="Monatsausgaben"></canvas>

                </div>

                <div class="col-lg-6">

                    <canvas id="Monatseinnahmen"></canvas>

                </div>

            </div>

            <div class="row">


                <div class="col-lg-6">
                    
                    <h4>Ausgaben je Kategorie</h4>

                    <canvas id="Kategorie_aus"></canvas>

                </div>

                <div class="col-lg-6">
                    
                    <h4>Einnahmen je Kategorie</h4>

                    <canvas id="Kategorie_ein"></canvas>

                </div>

            </div>


        </div>

    </jsp:attribute>

    <jsp:attribute name="script">

        <script type="text/javascript">

            var ctx = document.getElementById('Monatsausgaben').getContext('2d');
            var chart = new Chart(ctx, {
                // The type of chart we want to create
                type: 'line',
                // The data for our dataset
                data: {
                    labels: ["January", "February", "March", "April", "May", "June", "July"],
                    datasets: [{
                            label: "Monatsausgaben",
                            backgroundColor: 'rgb(255, 0, 0)',
                            borderColor: 'rgb(255, 0, 0)',
                            data: ${daten}
                        }]
                },
                // Configuration options go here
                options: {}
            });
        </script>

        <script type="text/javascript">

            var ctx = document.getElementById('Monatseinnahmen').getContext('2d');
            var chart = new Chart(ctx, {
                // The type of chart we want to create
                type: 'line',
                // The data for our dataset
                data: {
                    labels: ["January", "February", "March", "April", "May", "June", "July"],
                    datasets: [{
                            label: "Monatseinnahmen",
                            backgroundColor: 'rgb(0, 255, 0)',
                            borderColor: 'rgb(0, 255, 0)',
                            data: [0, 10, 5, 2, 20, 30, 45]
                        }]
                },
                // Configuration options go here
                options: {}
            });
        </script>

        <script type="text/javascript">
            var ctx = document.getElementById('Kategorie_aus').getContext('2d');
            var myDoughnutChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    datasets: [{
                            data: [10, 20, 30]
                            
                        }],

                    // These labels appear in the legend and in the tooltips when hovering different arcs
                    labels: [
                        'Freizeit',
                        'Benzin',
                        'Essen und Trinken'
                    ]

                },
                options: {}
            });
        </script>
        
        <script type="text/javascript">
            var ctx = document.getElementById('Kategorie_ein').getContext('2d');
            debugger;
            var myDoughnutChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    datasets: [{
                            data: [10, 20, 30]
                            
                        }],

                    // These labels appear in the legend and in the tooltips when hovering different arcs
                    labels: [
                        'Dividende',
                        'Nebenjob',
                        'Gehalt'
                    ]

                },
                options: {}
            });
        </script>


    </jsp:attribute>



</template:base>
