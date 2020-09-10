import React from 'react';

import ExchangeSelect from "../sharedcomponents/exchangeselect";
import StockTicker from "./stockticker";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            exchange: [],
            exchangeValue: "default",
            stock: [],
            stockValue: "default",
            stockTicker: [],
            stockSelectDisabled: true,
            stockTickerDisabled: true
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

        return (
            <div className="container">
                <ExchangeSelect exchangeValue={this.state.exchangeValue}
                                exchangeData={{exchange}}
                                onExchangeChange={this.setExchange}
                                stockDisabled={this.state.stockSelectDisabled ? 'disabled' : null}
                                stockValue={this.state.stockValue}
                                stockData={{stock}}
                                onStockChange={this.setStock}/>
                <StockTicker hidden={this.state.stockTickerDisabled ? 'hidden' : null} value={this.state.stockTicker.symbol}
                             data={{stockTicker}}/>

            </div>
        );
    }

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
        if (value !== undefined && value !== 'default') {
            this.setState({stockTickerDisabled: false});

            fetch("http://localhost:8080/stockexchange/api/exchange/" + this.state.exchangeValue + "/stock/"
                + value + "/ticker/latest")
                .then(data => data.json())
                .then(
                    (data) => {
                        this.setState({
                            stockTicker: data
                        });
                    }
                )
        } else {
            this.setState({stockTickerDisabled: true});
            this.setState({
                stockTicker: []
            })
        }
    }
}

export default Home;
