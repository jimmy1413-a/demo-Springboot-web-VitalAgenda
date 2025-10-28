// Historial Clínico List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#historialesTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[3, 'desc']], // Order by fecha descending
        columnDefs: [
            { orderable: false, targets: [5, 6] }
        ]
    });

    // Medical Records Chart
    const ctx = document.getElementById('medicalRecordsChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo'],
                datasets: [{
                    label: 'Registros Médicos',
                    data: [12, 19, 3, 5, 2], // Example data, replace with actual counts
                    backgroundColor: '#0d6efd'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
});

// Delete Historial Function
function deleteHistorial(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este historial clínico? Esta acción no se puede deshacer.')) {
        fetch('/api/historiales/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error eliminando historial clínico. Por favor, inténtalo de nuevo.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error eliminando historial clínico');
        });
    }
}