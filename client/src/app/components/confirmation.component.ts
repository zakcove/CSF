import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit {

  orderId: string = '';
  paymentId: string = '';
  total: number = 0;
  date: Date = new Date();

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state) {
      this.orderId = navigation.extras.state['orderId'];
      this.paymentId = navigation.extras.state['paymentId'];
      this.total = navigation.extras.state['total'];
      this.date = new Date();
    }
  }

  ngOnInit(): void {
    if (!this.orderId || !this.paymentId) {
      this.router.navigate(['/']);
    }
  }

  backToMenu() {
    this.router.navigate(['/']);
  }
}
