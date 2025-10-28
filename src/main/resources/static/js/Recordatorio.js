// Recordatorio List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#recordatoriosTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[2, 'desc']], // Order by fechaEnvio descending
        columnDefs: [
            { orderable: false, targets: [4, 5] }
        ]
    });

    // Reminder Status Chart
    const ctx = document.getElementById('reminderStatusChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Enviado', 'Pendiente'],
                datasets: [{
                    data: [8, 3], // Example data, replace with actual counts
                    backgroundColor: ['#198754', '#ffc107']
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

// Delete Recordatorio Function
function deleteRecordatorio(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este recordatorio? Esta acción no se puede deshacer.')) {
        fetch('/api/recordatorios/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error eliminando recordatorio. Por favor, inténtalo de nuevo.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error eliminando recordatorio');
        });
    }
}