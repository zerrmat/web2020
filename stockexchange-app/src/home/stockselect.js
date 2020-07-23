import React from "react";

class StockSelect extends React.Component {
    render() {
        return (
            <select disabled={this.props.disabled}>
                <option value="default">--- Choose Stock ---</option>
            </select>
        );
    }
}

export default StockSelect;