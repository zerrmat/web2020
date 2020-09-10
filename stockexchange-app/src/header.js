import React from "react";
import {Link} from "react-router-dom";

class Header extends React.Component {
    render() {
        return (
            <ul id="header-ul">
                <li className="headerPos" id="home-header-container">
                    <Link to="/stockexchange/home" id="home-header" className="link">Home</Link>
                </li>
                <li className="headerPos" id="history-header-container">
                    <Link to="/stockexchange/history" id="history-header" className="link">History</Link>
                </li>
            </ul>
        );
    }
}

export default Header;