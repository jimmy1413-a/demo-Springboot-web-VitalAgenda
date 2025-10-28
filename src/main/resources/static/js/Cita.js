// Cita List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#citasTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[0, 'asc']],
        columnDefs: [
            { orderable: false, targets: [6, 7] }
        ]
    });

    // Appointment Status Chart
    const ctx = document.getElementById('appointmentStatusChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['PENDIENTE', 'CONFIRMADA', 'CANCELADA'],
                datasets: [{
                    data: [5, 8, 2], // Example data, replace with actual counts
                    backgroundColor: ['#ffc107', '#198754', '#dc3545']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }
});

// Delete Cita Function
function deleteCita(id) {
    if (confirm('Are you sure you want to delete this appointment? This action cannot be undone.')) {
        fetch('/api/citas/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error deleting appointment. Please try again.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error deleting appointment');
        });
    }
}