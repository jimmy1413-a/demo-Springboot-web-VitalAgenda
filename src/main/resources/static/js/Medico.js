// Medico List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#medicosTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[1, 'asc']], // Order by nombre
        columnDefs: [
            { orderable: false, targets: [4, 5] }
        ]
    });

    // Specialty Distribution Chart
    const ctx = document.getElementById('specialtyDistributionChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Cardiología', 'Dermatología', 'Neurología', 'Pediatría', 'Ginecología'],
                datasets: [{
                    label: 'Médicos por Especialidad',
                    data: [3, 2, 4, 1, 2], // Example data, replace with actual counts
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

// Delete Medico Function
function deleteMedico(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este médico? Esta acción no se puede deshacer.')) {
        fetch('/api/medicos/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error eliminando médico. Asegúrate de que no tenga citas pendientes.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error eliminando médico');
        });
    }
}