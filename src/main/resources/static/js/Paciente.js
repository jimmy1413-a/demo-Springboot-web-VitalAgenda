// Paciente List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#pacientesTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[1, 'asc']], // Order by nombre
        columnDefs: [
            { orderable: false, targets: [5, 6] }
        ]
    });

    // Age Distribution Chart
    const ctx = document.getElementById('ageDistributionChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['18-30 años', '31-50 años', '51-70 años', '71+ años'],
                datasets: [{
                    data: [15, 25, 20, 10], // Example data, replace with actual counts
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

// Delete Paciente Function
function deletePaciente(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este paciente? Esta acción no se puede deshacer.')) {
        fetch('/api/pacientes/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error eliminando paciente. Asegúrate de que no tenga citas pendientes.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error eliminando paciente');
        });
    }
}