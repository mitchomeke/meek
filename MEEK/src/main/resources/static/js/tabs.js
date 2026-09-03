function alignTabPill(smooth = true){
    const pill = document.getElementById('tab-pill');
    const isActive = document.querySelector('.tab-link.is-active');
    if (!pill || !isActive) return;
}
const prevLeft = sessionStorage.getItem('meek_tab_left');
const prevWidth = sessionStorage.getItem('meek_tab_width');

if (prevLeft && prevWidth && smooth){
    pill.style.transition = 'none';
    pill.style.transform = `translateX(${prevLeft}px)`;
    pill.style.width = `${prevWidth}px`;

    pill.getBoundingClientRect();
    pill.style.transition = '';
}

requestAnimationFrame(() => {
    pill.style.transform = `translateX(${activeLink.offsetLeft}px)`;
    pill.style.width = `${activeLink.offsetWidth}px`;
    pill.style.opacity = '1';

    sessionStorage.setItem('meek_tab_left',activeLink.offsetLeft);
    sessionStorage.setItem('meek_tab_width',activeLink.offsetWidth);
});

document.addEventListener('click', (e) => {
    const link = e.target.closest('.tab-link');
    if (link){
        sessionStorage.setItem('meek_tab_left',link.offsetLeft);
        sessionStorage.setItem('meek_tab_right',link.offsetWidth);
    }
});

window.addEventListener('DOMContentLoaded', () => alignTabPill(true));
window.addEventListener('turbo:render', () => alignTabPill(true));
window.addEventListener('resize', () => alignTabPill(false));





