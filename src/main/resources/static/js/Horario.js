// Horario List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#horariosTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[1, 'asc']], // Order by diaSemana
        columnDefs: [
            { orderable: false, targets: [4, 5] }
        ]
    });

    // Schedule Distribution Chart
    const ctx = document.getElementById('scheduleDistributionChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'],
                datasets: [{
                    data: [5, 4, 6, 3, 2], // Example data, replace with actual counts
                    backgroundColor: ['#0d6efd', '#198754', '#ffc107', '#dc3545', '#6f42c1']
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

// Delete Horario Function
function deleteHorario(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este horario? Esta acción no se puede deshacer.')) {
        fetch('/api/horarios/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error eliminando horario. Por favor, inténtalo de nuevo.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error eliminando horario');
        });
    }
}