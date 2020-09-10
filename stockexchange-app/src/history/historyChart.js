import React from "react";

import CanvasJSReact from "../canvasjs.react";

var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class HistoryChart extends React.Component {
    constructor() {
        super();
        this.toggleDataSeries = this.toggleDataSeries.bind(this);
    }

    toggleDataSeries(e){
        if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
            e.dataSeries.visible = false;
        }
        else{
            e.dataSeries.visible = true;
        }
        this.chart.render();
    }

    Ready(props) {
        const history = props.data.stockHistory;
        if (history.length !== 0) {
            CanvasJS.addCultureInfo("pl",
                {
                    decimalSeparator: " ",
                    digitGroupSeparator: " ",
                    days: ["niedziela", "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota"],
                });

            let priceData = [];
            let volumeData = [];
            history.forEach(h => {
                priceData.push({
                    x: new Date(h.date),
                    y: h.value.number
                });
                volumeData.push({
                    x: new Date(h.date),
                    y: h.volume
                });
            });

            const options = {
                backgroundColor: "#C8E6C9",
                theme: "light2",
                animationEnabled: true,
                title:{
                    text: history[0].stockName
                },
                subtitles: [{
                    text: "Kliknij legendę aby pokazać / schować serię danych"
                }],
                axisX: {
                    title: "Data",
                    valueFormatString: "YYYY-MM-DD"
                },
                axisY: {
                    title: "Cena",
                    titleFontColor: "#000000",
                    lineColor: "#000000",
                    labelFontColor: "#000000",
                    tickColor: "#000000"
                },
                axisY2: {
                    title: "Ilość",
                    titleFontColor: "#2e7d32",
                    lineColor: "#2e7d32",
                    labelFontColor: "#2e7d32",
                    tickColor: "#2e7d32"
                },
                toolTip: {
                    shared: true
                },
                legend: {
                    cursor: "pointer",
                    itemclick: this.toggleDataSeries
                },
                data: [
                    {
                        color: "#000000",
                        lineColor: "#000000",
                        tickColor: "#000000",
                        type: "area",
                        name: "Cena",
                        showInLegend: true,
                        xValueFormatString: "YYYY-MM-DD",
                        yValueFormatString: "#.## " + history[0].exchangeCurrency,
                        dataPoints: priceData
                    },
                    {
                        color: "#2e7d32",
                        lineColor: "#2e7d32",
                        tickColor: "#2e7d32",
                        type: "line",
                        name: "Ilość",
                        axisYType: "secondary",
                        showInLegend: true,
                        xValueFormatString: "YYYY-MM-DD",
                        yValueFormatString: "#",
                        dataPoints: volumeData
                    }]
            };

            return (
                <div style={{marginTop: "8px", border: "5px solid #2e7d32"}}>
                    <CanvasJSChart options = {options}
                                   onRef={ref => this.chart = ref}
                                   culture = "pl"
                    />
                    {/*You can get reference to the chart instance as shown above using onRef. This allows you to access all chart properties and methods*/}
                </div>
            );
        } else {
            return <div id="chart" name="chart"></div>
        }
    }

    // from https://canvasjs.com/react-charts/multiseries-chart/
    render() {
        return (
            this.Ready(this.props)
        );
    }
}

export default HistoryChart;