import React from 'react';

import ExchangeSelect from "../sharedcomponents/exchangeselect";
import StockTicker from "../home/stockticker";
import StockHistory from "../history/stockhistory";
import HistoryChart from "./historyChart";

class History extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            exchange: [],
            exchangeValue: "default",
            stock: [],
            stockValue: "default",
            stockTicker: [],
            stockSelectDisabled: true,
            stockTickerDisabled: true,
            stockHistory: [],
            stockHistoryDisabled: true
        };
    }

    componentDidMount() {
        fetch("http://localhost:8080/stockexchange/api/exchange")
            .then(data => data.json())
            .then(
                (data) => {
                    this.setState({
                        exchange: data
                    });
                }
            )
    }

    render() {
        const {exchange} = this.state;
        const {stock} = this.state;
        const {stockTicker} = this.state;
        const {stockHistory} = this.state;

        return (
            <div className="container">
                <ExchangeSelect exchangeValue={this.state.exchangeValue}
                                exchangeData={{exchange}}
                                onExchangeChange={this.setExchange}
                                stockDisabled={this.state.stockSelectDisabled ? 'disabled' : null}
                                stockValue={this.state.stockValue}
                                stockData={{stock}}
                                onStockChange={this.setStock}/>
                <StockTicker hidden={this.state.stockTickerDisabled ? 'hidden' : null}
                             value={this.state.stockTicker.symbol} data={{stockTicker}}/>

                <div id="from-container">
                    <label htmlFor="from">Od </label>
                    <input type="date" id="from" name="from" max={this.initDate()}/>
                </div>
                <div id="to-container">
                    <label htmlFor="from">Do </label>
                    <input type="date" id="to" name="to" max={this.initDate()}/>
                </div>

                <div style={{margin: "0 auto", width: "100%"}}>
                    <input type="submit" value="Pobierz dane" onClick={this.historyRequest} style={{margin: "0 auto", display: "block"}}/>
                </div>

                <HistoryChart value={this.state.stockTicker.symbol} data={{stockHistory}}/>
            </div>
        );
    }

    historyRequest = () => {
        const exchange = this.state.exchangeValue;
        const stock = this.state.stockValue;
        const from = document.getElementById("from").value;
        const to = document.getElementById("to").value;

        const url = "http://localhost:8080/stockexchange/api/exchange/" + exchange + "/stock/" + stock
            + "/ticker/historical?from=" + from + "&to=" + to;
        fetch(url)
            .then(data => data.json())
            .then(
                (data) => {
                    this.setState({
                        stockHistory: data,
                        stockHistoryDisabled: true
                    });
                }
            );
    };

    initDate = (event) => {
        let today = new Date();
        let dd = today.getDate();
        let mm = today.getMonth() + 1; //January is 0!
        let yyyy = today.getFullYear();
        if(dd<10){
            dd='0'+dd
        }
        if(mm<10){
            mm='0'+mm
        }

        today = yyyy+'-'+mm+'-'+dd;
        return today;
    };

    setExchange = (value) => {
        this.setState({exchangeValue: value});
        this.setState({stockTickerDisabled: true});
        if (value !== undefined && value !== 'default') {
            this.setState({stockSelectDisabled: false});

            fetch("http://localhost:8080/stockexchange/api/exchange/" + value + "/stocks")
                .then(data => data.json())
                .then(
                    (data) => {
                        this.setState({
                            stock: data
                        });
                    }
                )
        } else {
            this.setState({
                stock: [],
                stockValue: "default",
                stockTicker: [],
                stockSelectDisabled: true,
            });
        }
    };

    setStock = (value) => {
        this.setState({stockValue: value});
    }
}

export default History;