import React from 'react';

import ExchangeSelect from "./exchangeselect";
import StockSelect from "./stockselect";

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            exchange: [],
            exchangeValue: "default",
            stockSelectDisabled: true
        };
    }

    componentDidMount() {
        fetch("http://localhost:8080/api/exchange")
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

        return (
            <div className="container">
                <ExchangeSelect value={this.state.exchangeValue} data={{exchange}}
                                onExchangeChange={this.setExchange}/>
                <StockSelect disabled={this.state.stockSelectDisabled ? 'disabled' : null}/>
            </div>
        );
    }

    setExchange = (value) => {
        this.setState({exchangeValue: value});
        if (value !== undefined && value !== 'default') {
            this.setState({stockSelectDisabled: false});
        } else {
            this.setState({stockSelectDisabled: true});
        }
    }
}

export default Home;
