import React from "react";

import Exchange from "./exchange";

class ExchangeSelect extends React.Component {
    render() {
        const exchange = this.props.data.exchange;

        return (
            <select value={this.props.value} name="exchangeSelect" id="exchangeSelect"
                    onChange={(event) => this.setExchange(event)}>
                <option value="default">--- Choose Stock Exchange ---</option>
                {exchange.map(exchange => <Exchange key={exchange.name} data={{exchange}}>{exchange}</Exchange>)}
            </select>
        );
    }

    setExchange(event) {
        //console.log("wybor: " + event.target.value);
        this.props.onExchangeChange(event.target.value);
    }
}

export default ExchangeSelect;