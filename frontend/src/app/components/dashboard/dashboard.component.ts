import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';
import { ItemService } from '../../services/item.service';
import { Item } from '../../interfaces/item.interface';
import { Order } from '../../interfaces/order.interface';
import { OrderService } from '../../services/order.service';
import { Chart } from 'chart.js/auto';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  constructor(private userService: UserService, private itemService: ItemService, private orderService: OrderService) { }
  user: User;
  items: Item[];
  orders: Order[];

  public chart_1: any;
  public chart_2: any;

  chart1DataPointsArray = [0, 0, 0, 0];

  ngOnInit(): void {
    this.userService.getUser();
    this.getItems();
    this.getOrders();
  }

  //Bar Chart
  getItems() {
    this.itemService.getItems().subscribe({
      next: (response) => {
        this.items = response.data;
        this.prepareItemsChart();
      }
    });
  }

  //Pie Chart
  getOrders() {
    this.orderService.getOrders().subscribe({
      next: (response) => {
        this.orders = response.data;
        this.preparePieChart();
      }
    });
  }

  prepareItemsChart() {
    this.chart_1 = new Chart('chart_1', {
      type: 'bar',
      data: {
        labels: ['cat 1', 'cat 2', 'cat 3', 'cat 4'],
        datasets: [
          {
            label: 'Categories',
            data: [10, 20, 30, 40],
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'], // Bar colors
            borderColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'], // Bar border colors
            borderWidth: 1, // Border thickness
            barThickness: 50, // Set the bar width (fixed value in pixels)
            maxBarThickness: 50, // Ensure bar width doesn't exceed this value
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: true, // Show legend
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
              display: false, // Remove vertical gridlines
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
              display: false, // Remove horizontal gridlines
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

    this.orders.forEach(order => {
      if (order.orderType === 'Incoming')
        incomingOrders += order.quantity;
      else
        outgoingOrders += order.quantity;
    })

    this.chart_2 = new Chart('chart_2', {
      type: 'pie',
      data: {
        labels: ['Incoming Orders', 'Outgoing Orders'], // Labels for the two types of data
        datasets: [
          {
            data: [incomingOrders, outgoingOrders], // Numbers corresponding to Incoming and Outgoing
            backgroundColor: ['#36A2EB', '#FF6384'], // Colors for the pie segments
            borderColor: ['#36A2EB', '#FF6384'], // Optional: Border colors
            borderWidth: 1, // Border thickness
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: true, // Show legend for the chart
            position: 'top', // Legend position: top, bottom, left, right
            labels: {
              font: {
                size: 14,
                family: 'Arial, sans-serif',
              },
              color: '#333', // Legend text color
            },
          },
          tooltip: {
            enabled: true, // Enable tooltips
            callbacks: {
              label: (tooltipItem) => {
                const value = tooltipItem.raw as number; // Access the raw value
                const total = (tooltipItem.dataset.data as number[]).reduce(
                  (acc, curr) => acc + curr,
                  0
                );
                const percentage = ((value / total) * 100).toFixed(2); // Calculate percentage
                return `${tooltipItem.label}: ${value} (${percentage}%)`; // Display value and percentage
              },
            },
          },
        },
      },
    });
  }


}
