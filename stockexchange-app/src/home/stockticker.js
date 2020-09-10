import React from "react";

class StockTicker extends React.Component {
    pad = function(data, size) {
        var s = String(data);
        let splits = s.split(".");
        if (splits.length > 1) {
            if (splits[1].length === 1) {
                s = s + "0";
            }
        }
        console.log(s);
        return s;
    }

    Ready(props) {
        const ticker = props.data.stockTicker;
        if (ticker.value != null) {
            return (
                <div id="ticker-container">
                    <table style={{margin: "0 auto"}}>
                        <thead style={{margin: "0 auto", borderBottom: "1px solid white"}}>
                            <tr>
                                <td><h2 style={{color: "#bdbdbd"}}>Nazwa</h2></td>
                                <td><h2 style={{color: "white"}}>Symbol</h2></td>
                                <td><h2 style={{color: "#bdbdbd"}}>Ostatnia cena</h2></td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><h1 id="ticker-name">{ticker.name}</h1></td>
                                <td><h1 id="ticker-symbol">({ticker.symbol})</h1></td>
                                <td><h2 id="ticker-value">{this.pad(ticker.value.number)}&nbsp;{ticker.value.currency.currencyCode}</h2></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            );
        } else {
            return (<div></div>);
        }
    }

    render() {
        return this.Ready(this.props);
    }
}

export default StockTicker;