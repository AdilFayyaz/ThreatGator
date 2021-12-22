
import signIn from "./signIn"
import addSource from "./addSource"
import "./App.css";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import Visualizer from "./visualizer"
import UserDashboard from "./dashboard";
import ViewConsumedData from "./ConsumerUI";
function App() {
let signIn1=new signIn();
let addSource1=new addSource();
// let dashboard1=new UserDashboard();
    return (
        <Router>
            <Routes>
                <Route path='/' element={new signIn().render()} />
                <Route path="/dashboard" element={<UserDashboard />} />
                <Route path="/addSource" element={addSource1.render()} />
                <Route path="/visualizer" element={<Visualizer />} />
                <Route path="/demo" element={<ViewConsumedData/>} />
            </Routes>

        </Router>


    );
}

export default App;