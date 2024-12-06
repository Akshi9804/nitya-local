import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';
import { AuthService } from '../../services/auth.service';
import { ItemService } from '../../services/item.service';
import { Item } from '../../interfaces/item.interface';
import { Order } from '../../interfaces/order.interface';
import { OrderService } from '../../services/order.service';
import { Chart } from 'chart.js/auto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule], // Add other required imports
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'], // Fixed typo
})
export class DashboardComponent implements OnInit {
  user: User | null = null;
  items: Item[] = [];
  orders: Order[] = [];
  barGraphData:any;

  public chart_1: any;
  public chart_2: any;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private itemService: ItemService,
    private orderService: OrderService
  ) { }

  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.getItems();
    console.log(this.items);
    this.getOrders();
    
    console.log(this.barGraphData);
  }

  signOut(){
    this.authService.logout();
  }

  getItems() {
    this.itemService.getItems().subscribe({
      next: (response) => {
        this.items = response.data || [];
        console.log(this.items);
        this.barGraphData = this.items.reduce((acc, item) => {
          acc[item.category] = (acc[item.category] || 0) + 1;
          return acc;
        }, {});
        console.log(this.barGraphData);
        this.prepareItemsChart();
      },
      error: (err) => {
        console.error('Error fetching items:', err);
      },
    });
  }

  getCategoriesWithCounts(): { [key: string]: number } {
    const categoryCounts: { [key: string]: number } = {};
  
    // Count items per category
    this.items.forEach((item) => {
      if (item.category) {
        categoryCounts[item.category] = (categoryCounts[item.category] || 0) + 1;
      }
    });
  
    return categoryCounts;
  }

  getOrders() {
    this.orderService.getOrders().subscribe({
      next: (response) => {
        this.orders = response.data || [];
        this.preparePieChart();
      },
      error: (err) => {
        console.error('Error fetching orders:', err);
      },
    });
  }

  prepareItemsChart() {
    const labels = Object.keys(this.barGraphData); 
  const data = Object.values(this.barGraphData);
  console.log(labels,data)
    this.chart_1 = new Chart('chart_1', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Categories',
            data: data,
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'],
            borderColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'],
            borderWidth: 1,
            barThickness: 50,
            maxBarThickness: 50,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: true,
            labels: {
              font: {
                size: 14,
                family: 'Arial, sans-serif',
              },
              color: '#333',
            },
          },
          tooltip: {
            enabled: true,
            backgroundColor: '#fff',
            bodyColor: '#000',
            borderColor: '#ccc',
            borderWidth: 1,
          },
        },
        scales: {
          x: {
            grid: {
              display: false,
            },
            ticks: {
              color: '#333',
              font: {
                size: 14,
                family: 'Arial, sans-serif',
              },
            },
          },
          y: {
            grid: {
              display: false,
            },
            ticks: {
              color: '#333',
              font: {
                size: 14,
                family: 'Arial, sans-serif',
              },
            },
          },
        },
      },
    });
  }

  preparePieChart() {
    let incomingOrders = 0;
    let outgoingOrders = 0;

    this.orders.forEach((order) => {
      if (order.orderType === 'Incoming') incomingOrders += order.quantity;
      else outgoingOrders += order.quantity;
    });

    this.chart_2 = new Chart('chart_2', {
      type: 'pie',
      data: {
        labels: ['Incoming Orders', 'Outgoing Orders'],
        datasets: [
          {
            data: [incomingOrders, outgoingOrders],
            backgroundColor: ['#36A2EB', '#FF6384'],
            borderColor: ['#36A2EB', '#FF6384'],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: true,
            position: 'top',
            labels: {
              font: {
                size: 14,
                family: 'Arial, sans-serif',
              },
              color: '#333',
            },
          },
          tooltip: {
            enabled: true,
            callbacks: {
              label: (tooltipItem) => {
                const value = tooltipItem.raw as number;
                const total = (tooltipItem.dataset.data as number[]).reduce(
                  (acc, curr) => acc + curr,
                  0
                );
                const percentage = ((value / total) * 100).toFixed(2);
                return `${tooltipItem.label}: ${value} (${percentage}%)`;
              },
            },
          },
        },
      },
    });
  }
}
