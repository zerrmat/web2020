import React from "react";

class StockHistory extends React.Component {
    Ready(props) {
        const history = props.data.stockHistory;
        console.log(history);
        if (history.length !== 0) {
            return (
                <div>
                    <table>
                        <thead>
                        <tr>
                            <th>Data</th>
                            <th>Cena</th>
                            <th>Ilość</th>
                        </tr>
                        </thead>
                        <tbody>
                        {history.map(history =>
                            <StockHistoryData key={history.date} data={{history}}>{history}</StockHistoryData>)}
                        </tbody>
                    </table>
                </div>
            );
        } else {
            return (<div>a</div>);
        }
    }

    render() {
        return this.Ready(this.props)
    }
}

class StockHistoryData extends React.Component {
    render() {
        const history = this.props.data.history;

        return (
            <tr>
                <td>{history.date}</td>
                <td>{history.value.number}&nbsp;{history.exchangeCurrency}</td>
                <td>{history.volume}</td>
            </tr>
        );
    }
}

export default StockHistory;