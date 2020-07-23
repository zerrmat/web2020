import React from "react";
import {Link} from "react-router-dom";

class Header extends React.Component {
    render() {
        return (
            <ul>
                <li>
                    <Link to="/home">Home</Link>
                </li>
                <li>
                    <Link to="/history">History</Link>
                </li>
            </ul>
        );
    }
}

export default Header;