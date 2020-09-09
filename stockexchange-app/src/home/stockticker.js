import React from "react";

class StockTicker extends React.Component {
    Ready(props) {
        const ticker = props.data.stockTicker;
        if (ticker.value != null) {
            return (
                <div>
                    <p>{ticker.id}</p>
                    <p>{ticker.name}</p>
                    <p>{ticker.symbol}</p>
                    <p>{ticker.value.number}</p>
                    <p>{ticker.value.currency.currencyCode}</p>
                </div>);
        } else {
            return (<div></div>);
        }
    }

    render() {
        return this.Ready(this.props);
    }
}

export default StockTicker;