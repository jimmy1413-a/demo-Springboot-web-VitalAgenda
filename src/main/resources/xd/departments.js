// Department List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#departmentsTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[0, 'asc']],
        columnDefs: [
            { orderable: false, targets: [4, 6] }
        ]
    });

    // Employee Distribution Chart
    const ctx = document.getElementById('employeeDistributionChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Engineering', 'Sales', 'Marketing', 'HR'],
                datasets: [{
                    data: [8, 5, 3, 2],
                    backgroundColor: ['#0d6efd', '#198754', '#ffc107', '#dc3545']
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

// Delete Department Function
function deleteDepartment(id) {
    if (confirm('Are you sure you want to delete this department? This action cannot be undone.')) {
        fetch('/api/v1/departments/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error deleting department. Make sure no employees are assigned.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error deleting department');
        });
    }
}