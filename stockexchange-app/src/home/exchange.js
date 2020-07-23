import React from "react";

class Exchange extends React.Component {
    render() {
        const exchange = this.props.data.exchange;

        return (
            <option value={exchange.name}>{exchange.name}</option>
        )

    }
}

export default Exchange;