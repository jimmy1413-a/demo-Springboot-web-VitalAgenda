// Usuario List JavaScript
$(document).ready(function() {
    // Initialize DataTable
    $('#usuariosTable').DataTable({
        pageLength: 10,
        responsive: true,
        order: [[1, 'asc']], // Order by nombre
        columnDefs: [
            { orderable: false, targets: [4, 5] }
        ]
    });

    // User Role Distribution Chart
    const ctx = document.getElementById('userRoleDistributionChart');
    if (ctx) {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['PACIENTE', 'MEDICO', 'ADMIN'],
                datasets: [{
                    label: 'Usuarios por Rol',
                    data: [20, 5, 2], // Example data, replace with actual counts
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

// Delete Usuario Function
function deleteUsuario(id) {
    if (confirm('¿Estás seguro de que quieres eliminar este usuario? Esta acción no se puede deshacer.')) {
        fetch('/api/usuarios/' + id, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Error eliminando usuario. Asegúrate de que no tenga datos asociados.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error eliminando usuario');
        });
    }
}