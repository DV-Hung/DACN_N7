// === SHARED UTILITIES FOR API CALLS WITH JWT AUTHENTICATION ===

// Flag to prevent multiple redirect alerts
let isRedirecting = false;

// --- Helper function để lấy access token từ localStorage ---
function getAccessToken() {
    return localStorage.getItem("access_token");
}

// --- Helper function để gọi API với token JWT ---
async function apiCall(url, options = {}) {
    const token = getAccessToken();
    if (!token) {
        if (!isRedirecting) {
            isRedirecting = true;
            alert("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại!");
            window.location.href = "login.html";
        }
        return null;
    }

    const headers = {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
        ...options.headers,
    };

    const response = await fetch(url, {
        ...options,
        headers,
        credentials: "include",
    });

    if (response.status === 401 || response.status === 403) {
        if (!isRedirecting) {
            isRedirecting = true;
            alert("Phiên đăng nhập hết hạn hoặc bạn không có quyền!");
            localStorage.removeItem("access_token");
            window.location.href = "login.html";
        }
        return null;
    }

    return response;
}

function renderPagination(containerId, meta, loadFunction) {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = '';

    const prev = document.createElement('button');
    prev.className = 'btn btn-sm btn-outline-primary';
    prev.textContent = '‹ Trước';
    prev.disabled = meta.page <= 1;
    prev.onclick = () => loadFunction(meta.page - 1, meta.pageSize);

    const next = document.createElement('button');
    next.className = 'btn btn-sm btn-outline-primary';
    next.textContent = 'Tiếp ›';
    next.disabled = meta.page >= meta.pages;
    next.onclick = () => loadFunction(meta.page + 1, meta.pageSize);

    const info = document.createElement('span');
    info.style.margin = '0 8px';
    info.textContent = `Trang ${meta.page} / ${meta.pages} — Tổng: ${meta.total}`;

    // page size selector
    const sizeSelect = document.createElement('select');
    sizeSelect.className = 'btn btn-sm btn-outline-secondary';
    sizeSelect.style.width = 'auto';
    sizeSelect.style.padding = '0.375rem 0.75rem';
    sizeSelect.style.fontSize = '0.875rem';
    sizeSelect.style.border = '1px solid #6c757d';
    sizeSelect.style.borderRadius = '0.25rem';
    sizeSelect.style.backgroundColor = 'white';
    sizeSelect.style.color = '#333';
    sizeSelect.style.cursor = 'pointer';
    [5, 10, 20, 50, 100].forEach(s => {
        const o = document.createElement('option');
        o.value = s;
        o.textContent = s + ' / trang';
        if (s === meta.pageSize) o.selected = true;
        sizeSelect.appendChild(o);
    });
    sizeSelect.onchange = (e) => loadFunction(1, Number(e.target.value));

    // numeric page buttons (centered around current page)
    const pagesWrapper = document.createElement('div');
    pagesWrapper.style.display = 'flex';
    pagesWrapper.style.gap = '6px';
    pagesWrapper.style.alignItems = 'center';

    const totalPages = Math.max(1, Number(meta.pages || 1));
    const current = Number(meta.page || 1);
    const maxButtons = 7; // show up to 7 page buttons
    let start = Math.max(1, current - Math.floor(maxButtons / 2));
    let end = start + maxButtons - 1;
    if (end > totalPages) {
        end = totalPages;
        start = Math.max(1, end - maxButtons + 1);
    }

    for (let p = start; p <= end; p++) {
        const btn = document.createElement('button');
        btn.className = 'btn btn-sm ' + (p === current ? 'btn-primary' : 'btn-outline-secondary');
        btn.textContent = p;
        btn.disabled = p === current;
        btn.onclick = () => loadFunction(p, meta.pageSize);
        pagesWrapper.appendChild(btn);
    }

    // assemble pagination controls
    container.appendChild(prev);
    container.appendChild(pagesWrapper);
    container.appendChild(info);
    container.appendChild(next);
    container.appendChild(sizeSelect);
}

