document.getElementById('username').addEventListener('input', debounce(async function () {
    const username = this.value;
    console.log("Checking username:", username); // Added logging

    try {
        const response = await fetch(`/data/checkUsername?username=${encodeURIComponent(username)}`);
        const result = await response.json();

        console.log("Fetch result:", result); // Added logging

        const signUpButton = document.querySelector('button[type="submit"]');
        if (result.exists) {
            signUpButton.disabled = true;
            Swal.fire({
                icon: 'warning',
                title: 'Username already exists!',
                text: 'Please choose a different username.',
                toast: true,
                position: 'bottom-end',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true
            });
        } else {
            signUpButton.disabled = false;
        }
    } catch (error) {
        console.error("Error fetching username:", error); // Added logging
    }
}, 500)); // Debounce for 500 milliseconds

// Debounce function to limit the rate of API calls
function debounce(func, delay) {
    let timer;
    return function (...args) {
        clearTimeout(timer);
        timer = setTimeout(() => func.apply(this, args), delay);
    };
}

// Handle form submission
document.getElementById('signUpForm').addEventListener('submit', async function (event) {
    event.preventDefault(); // Prevent default form submission

    const formData = new FormData(event.target);

    try {
        const response = await fetch('/data/signUpProcess', {
            method: 'POST',
            body: formData
        });

        const result = await response.json();

        console.log("Sign up result:", result); // Added logging

        if (result.flag === 'Success') {
            Swal.fire({
                icon: 'success',
                title: 'Signed up successfully!',
                toast: true,
                position: 'bottom-end',
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true,
                willClose: () => {
                    window.location.href = '/noc/login';
                }
            });
        } else {
            throw new Error('Sign up failed');
        }
    } catch (error) {
        Swal.fire({
            icon: 'error',
            title: 'Failed to sign up!',
            text: error.message,
            toast: true,
            position: 'bottom-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
    }
});
