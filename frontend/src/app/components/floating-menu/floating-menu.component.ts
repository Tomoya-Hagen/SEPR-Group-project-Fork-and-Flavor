import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-floating-menu',
  templateUrl: './floating-menu.component.html',
  styleUrls: ['./floating-menu.component.scss']
})
export class FloatingMenuComponent {
  @Input() options: { label: string, buttonClass: string, iconClass: string, action: () => void, disabled?: boolean }[] = [];
  menuOpen: boolean = false;
  isSpinning: boolean = false;

  toggleMenu() {
    this.isSpinning = !this.isSpinning;
    setTimeout(() => {
      this.menuOpen = !this.menuOpen;
    }, 200);
  }
}
