import React from "react";

class StockTicker extends React.component {
    render() {
        const ticker = this.props.data.ticker;

        return (
            <div>
                <p>{ticker.id}</p>
                <p>{ticker.name}</p>
                <p>{ticker.symbol}</p>
                <p>{ticker.value.number}</p>
                <p>{ticker.value.currency}</p>
            </div>
        );
    }
}

export default StockTicker;