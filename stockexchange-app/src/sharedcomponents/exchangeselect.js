import React from "react";

class ExchangeSelect extends React.Component {
    render() {
        const exchange = this.props.exchangeData.exchange;
        const stock = this.props.stockData.stock;

        return (
            <div name="exchangeDiv" id="exchangeDiv">
                <select value={this.props.exchangeValue} name="exchangeSelect" id="exchangeSelect"
                        onChange={(event) => this.setExchange(event)}>
                    <option value="default">--- Choose Stock Exchange ---</option>
                    {exchange.map(exchange => <Exchange key={exchange.symbol} data={{exchange}}>{exchange}</Exchange>)}
                </select>
                <select disabled={this.props.stockDisabled} value={this.props.stockValue} name="stockSelect"
                        id="stockSelect" onChange={(event) => this.setStock(event)}>
                    <option value="default">--- Choose Stock ---</option>
                    {stock.map(stock => <Stock key={stock.symbol} data={{stock}}>{stock}</Stock>)}
                </select>
            </div>
        );
    }

    setExchange(event) {
        //console.log("wybor: " + event.target.value);
        this.props.onExchangeChange(event.target.value);
    }

    setStock(event) {
        //console.log("wybor: " + event.target.value);
        this.props.onStockChange(event.target.value);
    }
}

class Exchange extends React.Component {
    render() {
        const exchange = this.props.data.exchange;

        return (
            <option value={exchange.symbol}>{exchange.name}</option>
        )

    }
}

class Stock extends React.Component {
    render() {
        const stock = this.props.data.stock;
        return (
            <option value={stock.symbol}>{stock.name == null ? stock.symbol : stock.name}</option>
        )
    }
}

export default ExchangeSelect;