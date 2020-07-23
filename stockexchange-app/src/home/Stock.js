import React from "react";

class Stock extends React.Component {
    render() {
        const stock = this.props.data.stock;
        return (
            <div>
                <p>{stock.id}</p>
                <p>{stock.name}</p>
                <p>{stock.value}</p>
                <p>{stock.currency}</p>
            </div>
        )
    }
}

export default Stock;