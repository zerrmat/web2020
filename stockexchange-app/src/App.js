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
            <div>
                <Router>
                    <div className="container">
                        <Header />
                        <Route exact path="/home" component={Home} />
                        <Route path="/history" component={History} />
                    </div>
                </Router>
            </div>
        )
    }
}

export default App;