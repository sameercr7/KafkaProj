const sliderWrapper = document.querySelector('.slider-wrapper');
let offset = 0;

function slideNext() {
    offset -= 25; // Adjust according to card width
    sliderWrapper.style.transform = `translateX(${offset}%)`;
    setTimeout(() => {
        const firstCard = sliderWrapper.querySelector('.testimonial-card:first-child');
        sliderWrapper.appendChild(firstCard);
        offset += 25;
        sliderWrapper.style.transition = 'none';
        sliderWrapper.style.transform = `translateX(${offset}%)`;
        setTimeout(() => {
            sliderWrapper.style.transition = 'transform 0.5s ease-in-out';
        }, 50);
    }, 500); // Match with the CSS transition duration
}

setInterval(slideNext, 3000); // Adjust the interval time as needed


document.addEventListener('DOMContentLoaded', () => {
    // Function to animate counters
    function animateCounter(counter, target) {
        const updateCounter = () => {
            const count = +counter.innerText;
            const increment = target / 200;  // Adjust the increment to control the speed
            if (count < target) {
                counter.innerText = `${Math.ceil(count + increment)}`;
                setTimeout(updateCounter, 10);  // Adjust the interval for smoother animation
            } else {
                counter.innerText = target;  // Ensure it ends exactly at the target value
            }
        };
        updateCounter();
    }

    // Function to fetch data and update the dashboard
    function updateLandingPageDashboard() {
        fetch('/data/statsForLandingPage')
            .then(response => response.json())
            .then(response => {
                const data = response.data;

                // Update and animate the counters with the data from the API
                const totalApplicationsElement = document.getElementById('totalApplicationsLanding');
                const inProgressApplicationsElement = document.getElementById('inProgressApplicationsLanding');
                const approvedApplicationsElement = document.getElementById('approvedApplicationsLanding');
                const rejectedApplicationsElement = document.getElementById('rejectedApplicationsLanding');

                animateCounter(totalApplicationsElement, data.totalApplications);
                animateCounter(inProgressApplicationsElement, data.inProgressApplications);
                animateCounter(approvedApplicationsElement, data.approvedApplications);
                animateCounter(rejectedApplicationsElement, data.rejectedApplications);
            })
            .catch(error => console.error('Error fetching dashboard stats:', error));
    }

    // Call the function to update the dashboard when the page loads
    updateLandingPageDashboard();
});

// Fetch data from API
// document.addEventListener('DOMContentLoaded', function() {
//     const sliderWrapper = document.getElementById('slider-wrapper');
//
//     // Fetch data from API
//     fetch('/data/feddbacklist')
//         .then(response => response.json())
//         .then(data => {
//             if (data.statusCode === 200 && data.data.success) {
//                 const feedbacks = data.data.feddbacks;
//                 feedbacks.forEach(feedback => {
//                     const card = document.createElement('div');
//                     card.classList.add('testimonial-card');
//
//                     card.innerHTML = `
//                         <div class="card5 p-3 text-center px-4">
//                             <div class="user-image d-flex align-items-center justify-content-center">
//                                 <img src="${feedback.uploadImage ? feedback.uploadImage : '/img/profile.png'}" class="rounded-circle mb-2" width="80" alt="${feedback.name}">
//                             </div>
//                             <div class="user-content">
//                                 <h5 class="fw-bold fs-4 mb-0">${feedback.agencyName}</h5>
//                                 <span class="fw-bold fs-5">${feedback.name}</span>
//                                 <p class="fw-bold fs-6">${feedback.description}</p>
//                             </div>
//                         </div>
//                     `;
//
//                     sliderWrapper.appendChild(card);
//                 });
//             } else {
//                 console.error('Error fetching data:', data.message);
//             }
//         })
//         .catch(error => console.error('Fetch error:', error));
// });

document.addEventListener('DOMContentLoaded', function() {
    const sliderWrapper = document.getElementById('slider-wrapper');

    // Fetch data from feedback API
    fetch('/data/feedbacklist')
        .then(response => response.json())
        .then(data => {
            if (data.statusCode === 200 && data.data.success) {
                const feedbacks = data.data.feddbacks;
                feedbacks.forEach(feedback => {
                    // Only display the card if the status is "show"
                    if (feedback.status === "show") {
                        const card = document.createElement('div');
                        card.classList.add('testimonial-card');

                        // Create the card structure
                        const cardContent = `
                            <div class="card5 p-3 text-center px-4">
                                <div class="user-image d-flex align-items-center justify-content-center">
                                    <img id="image-${feedback.id}" class="rounded-circle mb-2" width="80" alt="${feedback.name}">
                                </div>
                                <div class="user-content">
                                    <h5 class="fw-bold fs-4 mb-0">${feedback.agencyName}</h5>
                                    <span class="fw-bold fs-5">${feedback.name}</span>
                                    <p class="fw-bold fs-6">${feedback.description}</p>
                                </div>
                            </div>
                        `;

                        card.innerHTML = cardContent;
                        sliderWrapper.appendChild(card);

                        // Fetch image data for each feedback
                        fetchImage(feedback.id);
                    }
                });
            } else {
                console.error('Error fetching data:', data.message);
            }
        })
        .catch(error => console.error('Fetch error:', error));
});

function fetchImage(feedbackId) {
    const imageElement = document.getElementById(`image-${feedbackId}`);
    const imageUrl = `/data/viewfeedbackimage/${feedbackId}`;

    fetch(imageUrl)
        .then(response => response.blob())
        .then(blob => {
            const objectURL = URL.createObjectURL(blob);
            imageElement.src = objectURL;
        })
        .catch(error => {
            console.error('Error fetching image:', error);
            // Optionally set a default image or placeholder
            imageElement.src = '/img/profile.png';
        });
}
