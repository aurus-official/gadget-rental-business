import FilledStar from "../assets/filled-star.svg";
import UnfilledStar from "../assets/unfilled-star.svg";
import HalfFilledStar from "../assets/half-filled-star.svg"
import "../styles/reviewcard.css";

function ReviewCard({ cardDatum: { profile_name, rating, initials, review, location, rate } }) {
    if (rating > 5) {
        throw new Error("Rating shouldn't exceed 5.");
    }

    const ratingsImg = [];
    let ratingNum = parseFloat(rating);


    for (let i = 0; i < 5; ++i) {
        if ((ratingNum - 1) >= 0) {
            ratingNum -= 1;
            ratingsImg.push(<img key={i} src={FilledStar} alt="filled-star" />);
            continue;
        }

        if ((ratingNum - 0.5) >= 0) {
            ratingNum -= 0.5;
            ratingsImg.push(<img key={i} src={HalfFilledStar} alt="half-filled-star" />);
            continue;
        }

        ratingsImg.push(<img key={i} src={UnfilledStar} alt="unfilled-star" />);
    }

    return (
        <div className="review-card-container">
            <div className="star-container">
                {ratingsImg}
            </div>
            <h3 className="review-text">{review}</h3>
            <hr className="review-horizontal-line" />
            <div className="review-profile-container">
                <div className="review-profile-icon">
                    <p className="review-initials">{initials(profile_name)}</p>
                </div>
                <div className="review-details">
                    <p className="review-subtext">{profile_name}</p>
                    <p className="review-location">{location}</p>
                </div>
            </div>
        </div>
    )
}

export default ReviewCard;
