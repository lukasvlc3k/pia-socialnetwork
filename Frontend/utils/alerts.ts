import Swal, { SweetAlertIcon, SweetAlertPosition } from 'sweetalert2';
import withReactContent from 'sweetalert2-react-content';

const MySwal = withReactContent(Swal);

export function ShowToast(
    text: string,
    icon: SweetAlertIcon = 'success',
    timer = 3000,
    position: SweetAlertPosition = 'top-right'
) {
    const Toast = Swal.mixin({
        toast: true,
        position: position,
        showConfirmButton: false,
        timer: timer,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
        },
    });

    Toast.fire({
        icon: icon,
        title: text,
    });
}
