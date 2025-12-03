import RentalSample from "../assets/rental-sample.png";
import "../styles/rentalcard.css";

function RentalCard({ productImage, productName }) {
    return (
        <div className="rental-card-container">
            <img alt="rental-card-image" src={RentalSample} />
            <h5 className="rental-card-text">NovaBook 14 Pro</h5>
        </div>
    )
}

export default RentalCard;
