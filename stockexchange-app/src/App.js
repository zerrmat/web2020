import React from 'react';
import {
    BrowserRouter as Router,
    Route
} from 'react-router-dom';
import Home from "./home/home";
import History from "./history/history";
import Header from "./header";

class App extends React.Component {
    render() {
        return (
            <div id="main-container">
                <div id="logo" name="logo">
                    <span id="logo-text" name="logo-text">Stock Exchange</span>
                </div>
                <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                    <div className="container">
                        <Header />
                        <Route exact path="/stockexchange/home" component={Home} />
                        <Route path="/stockexchange/history" component={History} />
                    </div>
                </Router>
            </div>
        )
    }
}

export default App;