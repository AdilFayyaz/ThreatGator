
import signIn from "./signIn"
// import addSource from "./addSource"
import "./App.css";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";

import UserDashboard from "./dashboard";

function App() {
let signIn1=new signIn();
// let addSource1=new addSource();
// let dashboard1=new UserDashboard();
    return (
        <Router>

            <Routes>
                {/*<Route exact path='/' component={Home} />*/}
                {/*<Route path='/contact' component={Contact} />*/}
                {/*<Route path='/about' component={About} />*/}
                <Route path='/' element={new signIn().render()} />

                <Route path="/dashboard" element={<UserDashboard />} />
                {/* <Route path="/addSource" element={addSource1.render()} /> */}
                {/* <Route path="*" element={<ErrorPage />} /> */}
            </Routes>

        </Router>


    );
}

export default App;